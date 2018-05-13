package io.graversen.trunk.io;

import io.graversen.trunk.io.serialization.PrettyPrintGsonSerializer;
import io.graversen.trunk.io.serialization.interfaces.ISerializer;
import io.graversen.trunk.os.OSUtils;

import java.io.*;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.UUID;

public final class IOUtils
{
    private final String TEMPORARY_DIRECTORY_PATH = System.getProperty("java.io.tmpdir");
    private final String PROJECT_DIRECTORY_NAME;

    private final ISerializer serializer;
    private final OSUtils osUtils;

    public final String DEFAULT_WINDOWS_PATH;
    public final String DEFAULT_MAC_PATH;
    public final String DEFAULT_LINUX_PATH;
    public final String DEFAULT_UNKNOWN_PATH;

    public IOUtils()
    {
        this(null);
    }

    public IOUtils(String projectDirectoryName)
    {
        this(projectDirectoryName, new PrettyPrintGsonSerializer());
    }

    public IOUtils(String projectDirectoryName, ISerializer serializer)
    {
        this.osUtils = new OSUtils();
        this.serializer = serializer;

        this.PROJECT_DIRECTORY_NAME = projectDirectoryName;
        this.DEFAULT_WINDOWS_PATH = Paths.get(System.getenv("APPDATA"), PROJECT_DIRECTORY_NAME).toString();
        this.DEFAULT_MAC_PATH = Paths.get(File.separator, "usr", "local", PROJECT_DIRECTORY_NAME).toString();
        this.DEFAULT_LINUX_PATH = Paths.get(File.separator, "etc", PROJECT_DIRECTORY_NAME).toString();
        this.DEFAULT_UNKNOWN_PATH = DEFAULT_LINUX_PATH;
    }

    private void validateProjectName()
    {
        if (PROJECT_DIRECTORY_NAME == null)
        {
            throw new IllegalStateException(String.format("Please initialise %s with a project name", getClass().getSimpleName()));
        }
    }

    private void createIfNotExists(Path path)
    {
        if (!Files.exists(path))
        {
            try
            {
                Files.createDirectories(path);
            }
            catch (IOException e)
            {
                throw new RuntimeException("Could not create directories", e);
            }
        }
    }

    public Path getStorageDirectory()
    {
        validateProjectName();
        final Path storageDirectoryPath = Paths.get(getProjectDirectory().toString(), ".storage");
        createIfNotExists(storageDirectoryPath);
        return storageDirectoryPath;
    }

    public Path getProjectDirectory()
    {
        validateProjectName();
        final Path projectDirectoryPath = getProjectDirectoryPath();
        createIfNotExists(projectDirectoryPath);
        return projectDirectoryPath;
    }

    private Path getProjectDirectoryPath()
    {
        switch (osUtils.getOperatingSystem())
        {
            case Windows:
                return Paths.get(System.getenv("APPDATA"), PROJECT_DIRECTORY_NAME);

            case Linux:
                return Paths.get("etc", PROJECT_DIRECTORY_NAME);

            default:
                throw new RuntimeException(String.format("%s is not supported at the moment", osUtils.getOperatingSystem()));
        }
    }

    /**
     * Gets the base path for the system. This is the root path for all the system's flat file needs.
     *
     * @return Path object representing the base path
     */
    public Path getBasePath()
    {
        switch (osUtils.getOperatingSystem())
        {
            case Windows:
                return Paths.get(DEFAULT_WINDOWS_PATH);
            case MacOS:
                return Paths.get(DEFAULT_MAC_PATH);
            case Linux:
                return Paths.get(DEFAULT_LINUX_PATH);
            case Unknown:
                return Paths.get(DEFAULT_UNKNOWN_PATH);
            default:
                return Paths.get(DEFAULT_UNKNOWN_PATH);
        }
    }

    /**
     * Gets the temporary path for the system. The temporary folder resides in the base path of the system, but it is intended for temporary usage and thus may easily be cleaned.
     *
     * @return Path object representing the temporary path
     */
    public Path getTemporaryDirectoryPath()
    {
        return Paths.get(TEMPORARY_DIRECTORY_PATH);
    }

    public Path getTemporaryDirectoryPath(String fileName)
    {
        return Paths.get(TEMPORARY_DIRECTORY_PATH, fileName);
    }

    /**
     * Deserializes an object from the filesystem, by referencing the file's name.
     *
     * @param fileName      The file's name
     * @param targetClass   The target class for the object
     * @param serializeMode How the serialization should be done, i.e. JSON or as Java objects
     * @param <T>
     * @return The deserialized object
     * @throws RuntimeException Thrown if an error occurs during the process
     */
    public <T> T deserialize(String fileName, Class<T> targetClass, SerializeMode serializeMode)
    {
        return deserialize(Paths.get(getBasePath().toString(), fileName), targetClass, serializeMode);
    }

    /**
     * Deserializes an object from the filesystem, by referencing the file's path.
     *
     * @param path          The file's path
     * @param targetClass   The target class for the object
     * @param serializeMode How the serialization should be done, i.e. JSON or as Java objects
     * @param <T>
     * @return The deserialized object
     * @throws RuntimeException Thrown if an error occurs during the process
     */
    public <T> T deserialize(Path path, Class<T> targetClass, SerializeMode serializeMode)
    {
        if (!Files.exists(path)) throw new IllegalArgumentException("No file exists at " + path.toString());
        if (Files.isDirectory(path)) throw new IllegalArgumentException(path.toString() + " is a directory");

        switch (serializeMode)
        {
            case OBJECT:
                try (ObjectInputStream objectInputStream = new ObjectInputStream(new FileInputStream(path.toFile())))
                {
                    return targetClass.cast(objectInputStream.readObject());
                }
                catch (IOException | ClassNotFoundException e)
                {
                    throw new RuntimeException("Could not deserialize object", e);
                }

            case JSON:
                return serializer.deserialize(read(path).toString(), targetClass);
        }

        return null;
    }

    /**
     * Serializes an object, persisting it to the filesystem of the machine.
     *
     * @param fileName      The file's name
     * @param object        The object to be serialized
     * @param serializeMode How the serialization should be done, i.e. JSON or as Java objects
     * @return Path object representing where the serialized object was stored
     * @throws RuntimeException Thrown if an error occurs during the process
     */
    public Path serialize(String fileName, Object object, SerializeMode serializeMode)
    {
        return serialize(Paths.get(getBasePath().toString(), fileName), object, serializeMode);
    }

    /**
     * Serializes an object, persisting it to the filesystem of the machine.
     *
     * @param path          The file's path
     * @param object        The object to be serialized
     * @param serializeMode How the serialization should be done, i.e. JSON or as Java objects
     * @return Path object representing where the serialized object was stored
     * @throws RuntimeException Thrown if an error occurs during the process
     */
    public Path serialize(Path path, Object object, SerializeMode serializeMode)
    {
        if (serializeMode == SerializeMode.OBJECT && !(object instanceof Serializable))
        {
            throw new IllegalArgumentException("The source object must be serializable to serialize as a Java object");
        }

        if (!Files.exists(path))
        {
            newFile(path);
        }

        switch (serializeMode)
        {
            case OBJECT:
                try (ObjectOutputStream objectOutputStream = new ObjectOutputStream(new FileOutputStream(path.toFile())))
                {
                    objectOutputStream.writeObject(object);
                }
                catch (IOException e)
                {
                    throw new RuntimeException("Could not serialize object", e);
                }
                break;

            case JSON:
                write(path, serializer.serialize(object), WriteMode.OVERWRITE);
                break;
        }

        return path;
    }

    /**
     * Writes a string to the specified file. If the file <i>may</i> not exist, the developer must choose the appropriate {@code WriteMode}, e.g. {@code OVERWRITE}.
     *
     * @param fileName  The file's name
     * @param string    The string to be written to the file
     * @param writeMode How the write process should be done
     * @return Path object representing the final file's location
     * @throws RuntimeException Thrown if anything goes wrong during the write
     */
    public Path write(String fileName, String string, WriteMode writeMode)
    {
        if (fileName == null || fileName.isEmpty())
        {
            throw new IllegalArgumentException("The argument 'fileName' must be valid.");
        }

        if (string == null || string.isEmpty())
        {
            throw new IllegalArgumentException("The argument 'string' must be valid.");
        }

        return write(Paths.get(getProjectDirectory().toString(), fileName), string, writeMode);
    }

    /**
     * Writes a string to the specified file. If the file <i>may</i> not exist, the developer must choose the appropriate {@code WriteMode}, e.g. {@code OVERWRITE}.
     *
     * @param path      The file's path
     * @param string    The string to be written to the file
     * @param writeMode How the write process should be done
     * @return Path object representing the final file's location
     * @throws RuntimeException Thrown if anything goes wrong during the write
     */
    public Path write(Path path, String string, WriteMode writeMode)
    {
        StandardOpenOption openOption = StandardOpenOption.WRITE;

        switch (writeMode)
        {
            case APPEND:
                openOption = StandardOpenOption.APPEND;
                break;
            case OVERWRITE:
                delete(path, DeleteMode.SHALLOW);
                openOption = StandardOpenOption.CREATE_NEW;
                break;
        }

        try
        {
            return Files.write(path, string.getBytes(), openOption);
        }
        catch (IOException e)
        {
            throw new RuntimeException("Could not write to file", e);
        }
    }

    /**
     * Reads the contents of the specified file.
     *
     * @param fileName The file's name
     * @return StringBuilder containing all the data of the file
     * @throws RuntimeException Thrown if the file could not be read
     */
    public StringBuilder read(String fileName)
    {
        if (fileName == null || fileName.isEmpty())
        {
            throw new IllegalArgumentException("The argument 'fileName' must be valid.");
        }

        return read(Paths.get(getBasePath().toString(), fileName));
    }

    /**
     * Reads the contents of the specified file.
     *
     * @param path The file's path
     * @return StringBuilder containing all the data of the file
     * @throws RuntimeException Thrown if the file could not be read
     */
    public StringBuilder read(Path path)
    {
        StringBuilder stringBuilder = new StringBuilder();
        try
        {
            Files.readAllLines(path).forEach(s -> stringBuilder.append(s).append("\n"));
        }
        catch (IOException e)
        {
            throw new RuntimeException("Could not read file", e);
        }

        return stringBuilder;
    }

    /**
     * Deletes the specified file or folder in the filesystem.
     *
     * @param fileName   The file's (or folder's) name
     * @param deleteMode Determines how the delete procedure should be executed; e.g. mode {@code RECURSIVE} must be used to delete folders
     * @return true if the delete operation executed, otherwise false
     * @throws RuntimeException Thrown is the file could not be deleted
     */
    public boolean delete(String fileName, DeleteMode deleteMode)
    {
        if (fileName == null || fileName.isEmpty())
        {
            throw new IllegalArgumentException("The argument 'fileName' must be valid.");
        }

        return delete(Paths.get(getBasePath().toString(), fileName), deleteMode);
    }

    /**
     * Deletes the specified file or folder in the filesystem.
     *
     * @param path       The file's (or folder's) path
     * @param deleteMode Determines how the delete procedure should be executed; e.g. mode {@code RECURSIVE} must be used to delete folders
     * @return true if the delete operation executed, otherwise false
     * @throws RuntimeException Thrown is the file could not be deleted
     */
    public boolean delete(Path path, DeleteMode deleteMode)
    {
        try
        {
            switch (deleteMode)
            {
                case SHALLOW:
                    if (Files.isDirectory(path)) return false;

                    return Files.deleteIfExists(path);

                case RECURSIVE:
                    if (!Files.isDirectory(path)) return false;

                    Files.walkFileTree(path, new SimpleFileVisitor<Path>()
                    {
                        @Override
                        public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException
                        {
                            Files.delete(file);
                            return FileVisitResult.CONTINUE;
                        }

                        @Override
                        public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException
                        {
                            Files.delete(dir);
                            return FileVisitResult.CONTINUE;
                        }
                    });

                    boolean result = !Files.exists(getTemporaryDirectoryPath());
                    Files.createDirectory(getTemporaryDirectoryPath());
                    return result;

                default:
                    return false;
            }
        }
        catch (IOException e)
        {
            throw new RuntimeException("Could not delete file", e);
        }
    }

    private Path getStoragePath(String key)
    {
        return Paths.get(getStorageDirectory().toString(), String.format("%s.json", key));
    }

    public void toStorage(String key, Object value)
    {
        final Path storagePath = getStoragePath(key);
        final String objectJson = serializer.serialize(value);

        write(storagePath, objectJson, WriteMode.OVERWRITE);
    }

    public void deleteFromStorage(String key)
    {
        final Path storagePath = getStoragePath(key);
        delete(storagePath, DeleteMode.SHALLOW);
    }

    public <T> T fromStorage(String key, Class<T> targetClass)
    {
        final Path storagePath = getStoragePath(key);
        final StringBuilder objectJson = read(storagePath);

        return serializer.deserialize(objectJson.toString(), targetClass);
    }

    /**
     * Creates a new directory in the {@code temp} directory of the application, with an {@code UUID} as the name.
     *
     * @return Path object representing the newly created directory
     * @throws RuntimeException Thrown if the directory could not be created
     */
    public Path newTempDir()
    {
        try
        {
            return Files.createDirectory(Paths.get(getTemporaryDirectoryPath().toString(), UUID.randomUUID().toString()));
        }
        catch (IOException e)
        {
            throw new RuntimeException("Could not create temporary directory", e);
        }
    }

    /**
     * Creates a new file in the {@code temp} directory of the application, with an {@code UUID} as the name, and {@code tmp} as the file extension.
     *
     * @return Path object representing the newly created file
     * @throws RuntimeException Thrown if the file could not be created
     */
    public Path newTempFile()
    {
        return newTempFile("tmp");
    }

    /**
     * Creates a new file in the {@code temp} directory of the application, with an {@code UUID} as the name.
     *
     * @param extension The file extension to be used
     * @return Path object representing the newly created file
     * @throws RuntimeException Thrown if the file could not be created
     */
    public Path newTempFile(String extension)
    {
        if (extension == null || extension.isEmpty())
        {
            throw new IllegalArgumentException("The argument 'extension' must be valid.");
        }
        else if (extension.length() >= (255 - 36))
        {
            throw new IllegalArgumentException("The argument 'extension' must less than " + (255 - 36) + " characters long.");
        }

        try
        {
            return Files.createFile(Paths.get(getTemporaryDirectoryPath().toString(), UUID.randomUUID().toString() + "." + extension));
        }
        catch (IOException e)
        {
            throw new RuntimeException("Could not create temporary file", e);
        }
    }

    /**
     * Spawns a new file. Use it for whatever you want, mate.
     *
     * @param fileName The file's name
     * @return Path object representing the newly created file
     * @throws RuntimeException Thrown if the file could not be created
     */
    public Path newFile(String fileName)
    {
        if (fileName == null || fileName.isEmpty())
        {
            throw new IllegalArgumentException("The argument 'fileName' must be valid.");
        }

        return newFile(Paths.get(getBasePath().toString(), fileName));
    }

    /**
     * Spawns a new file. Use it for whatever you want, mate.
     *
     * @param path The file's path
     * @return Path object representing the newly created file
     * @throws RuntimeException Thrown if the file could not be created
     */
    public Path newFile(Path path)
    {
        createIfNotExists(path);
        try
        {
            return Files.createFile(path);
        }
        catch (IOException e)
        {
            throw new RuntimeException("Could not create file", e);
        }
    }

    /**
     * Checks if a particular file exists.
     *
     * @param fileName The file's name
     * @return true if the file exists, false otherwise
     */
    public boolean fileExists(String fileName)
    {
        if (fileName == null || fileName.isEmpty())
        {
            throw new IllegalArgumentException("The argument 'fileName' must be valid.");
        }

        return fileExists(Paths.get(getBasePath().toString(), fileName));
    }

    /**
     * Checks if a particular file exists.
     *
     * @param path The file's path
     * @return true if the file exists, false otherwise
     */
    public boolean fileExists(Path path)
    {
        return Files.exists(path);
    }

    /**
     * <b>Deletes</b> all contents of the application's {@code temp} directory.
     *
     * @return true if the delete operation executed, false otherwise
     * @throws RuntimeException Thrown if the files could not be cleared
     */
    public boolean clearTempFiles()
    {
        return delete(getTemporaryDirectoryPath(), DeleteMode.RECURSIVE);
    }

    /**
     * Describes how a write operation should be executed.
     */
    public enum WriteMode
    {
        /**
         * Strings should be appended to the end of the file.
         */
        APPEND,

        /**
         * The file should be overwritten <i>or created</i> if it does not exist.
         */
        OVERWRITE
    }

    /**
     * Describes how a delete operation should be executed.
     */
    public enum DeleteMode
    {
        /**
         * Allows only "top level" files to be deleted, i.e. will fail on directories.
         */
        SHALLOW,

        /**
         * Allows files of any depth to be deleted, i.e. will <i>not</i> fail on directories.
         */
        RECURSIVE
    }

    /**
     * Describes how serialization should be executed.
     */
    public enum SerializeMode
    {
        /**
         * The object should be serialized as a "Java object", i.e. the standard "Serializable" way of the Java platform.
         */
        OBJECT,

        /**
         * The object should be serialized as a JSON object.
         */
        JSON
    }
}

