package ee.ut.jf2014.homework2;

import static java.nio.file.StandardWatchEventKinds.ENTRY_CREATE;
import static java.nio.file.StandardWatchEventKinds.ENTRY_DELETE;
import static java.nio.file.StandardWatchEventKinds.ENTRY_MODIFY;
import static java.nio.file.StandardWatchEventKinds.OVERFLOW;
import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.nio.file.WatchEvent;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;
import java.nio.file.attribute.FileAttribute;

public class DirectoryMirror {

  private final Path source;
  private final Path target;

  public DirectoryMirror(Path source, Path target) throws IOException {
    this.source = source;
    this.target = target;
    if (!Files.isDirectory(target)) {
        System.out.println("Creating directory " + target.toAbsolutePath());
        Files.createDirectories(target);
    }
    syncDirectories();
  }

  private void syncDirectories() throws IOException {
    try (DirectoryStream<Path> sourceStream = Files.newDirectoryStream(source);
         DirectoryStream<Path> targetStream = Files.newDirectoryStream(target)) {
      System.out.println("Syncing source and target directory");
      deleteUnsyncedfilesInTargetDir(targetStream);
      copyUnsyncedFilesToTargetDir(sourceStream);
      System.out.println("Sync completed");
    }
  }
  
  private void deleteUnsyncedfilesInTargetDir(DirectoryStream<Path> targetStream) throws IOException {
    for (Path targetFile : targetStream) {
      if (Files.isRegularFile(targetFile)) {
        Path sourceFile = concatFileToDir(source, targetFile);
        if (fileNotExists(sourceFile)) {
          deleteFile(targetFile);
        }
      }
    }
  }
  
  private void copyUnsyncedFilesToTargetDir(DirectoryStream<Path> sourceStream) throws IOException {
    for (Path sourceFile : sourceStream) {
      if (Files.isRegularFile(sourceFile)) {
        Path targetFile = concatFileToDir(target, sourceFile);

        if (fileNotExists(targetFile)) {
          createFile(sourceFile, targetFile);
        } else if (isTargetOutdated(sourceFile, targetFile)) {
          updateFile(sourceFile, targetFile);
        }
      }
    }
  }
  
  private boolean isTargetOutdated(Path sourceFile, Path targetFile) throws IOException {
    return Files.getLastModifiedTime(sourceFile).compareTo(Files.getLastModifiedTime(targetFile)) > 0;
  }
  
  private Path concatFileToDir(Path dir, Path file) {
    return dir.resolve(file.getFileName());
  }
  
  private boolean fileNotExists(Path targetFile) {
    return !Files.exists(targetFile, LinkOption.NOFOLLOW_LINKS);
  }

  private void createFile(Path sourceFile, Path targetFile) throws IOException {
    copyFiles(sourceFile, targetFile);
    System.out.println("Copied file from " + sourceFile.toAbsolutePath() + " to " + targetFile.toAbsolutePath());
  }
  
  private void updateFile(Path sourceFile, Path targetFile) throws IOException {
    copyFiles(sourceFile, targetFile);
    System.out.println("Modified file from " + sourceFile.toAbsolutePath() + " to " + targetFile.toAbsolutePath());
  }
  
  private void deleteFile(Path targetFile) throws IOException {
    if (Files.isRegularFile(targetFile) && Files.deleteIfExists(targetFile)) {
      System.out.println("Deleted file " + targetFile.toAbsolutePath());
    }
  }
  
  private void copyFiles(Path sourceFile, Path targetFile) throws IOException {
    Files.copy(sourceFile, targetFile, StandardCopyOption.REPLACE_EXISTING);
  }
  
  
  public void watchEvents() throws IOException {
    System.out.println("Starting watch service");
    WatchService watcher = FileSystems.getDefault().newWatchService();
    WatchKey key = source.register(watcher, ENTRY_CREATE, ENTRY_DELETE, ENTRY_MODIFY);

    while (true) {
      try {
        key = watcher.take();
      } catch (InterruptedException e) {
        System.err.println("Watch service interrupted");
        return;
      }

      for (WatchEvent<?> event : key.pollEvents()) {
        WatchEvent.Kind<?> kind = event.kind();

        if (kind == OVERFLOW) {
          continue;
        }

        Path fileName = getEventFileName(event);
        Path sourceFile = concatFileToDir(source, fileName);
        Path targetFile = concatFileToDir(target, fileName);

        if (kind == ENTRY_DELETE) {
          deleteFile(targetFile);
        }

        if (Files.isRegularFile(sourceFile)) {
          if (kind == ENTRY_CREATE) {
            createFile(sourceFile, targetFile);
          } else if (kind == ENTRY_MODIFY) {
            updateFile(sourceFile, targetFile);
          }
        }
      }

      boolean valid = key.reset();
      if (!valid) {
        System.out.println("Watch service stopped");
        break;
      }
    }
  }
  
  private Path getEventFileName(WatchEvent<?> event) {
    return ((WatchEvent<Path>) event).context();
  }
}
