package io.graversen.trunk.io;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import io.graversen.trunk.os.OSUtils;

import java.io.*;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.UUID;

public final class IOUtils
{
    private final String TEMPORARY_DIRECTORY_PATH = System.getProperty("java.io.tmpdir");
    private final String PROJECT_DIRECTORY_NAME;

    private final Gson gson;
    private final OSUtils osUtils;

    public final String DEFAULT_WINDOWS_PATH;
    public final String DEFAULT_MAC_PATH;
    public final String DEFAULT_LINUX_PATH;
    public final String DEFAULT_UNKNOWN_PATH;

    public IOUtils()
    {
        this(UUID.randomUUID().toString());
    }

    public IOUtils(String projectDirectoryName)
    {
        this(projectDirectoryName, defaultGsonBuilder());
    }

    public IOUtils(String projectDirectoryName, GsonBuilder gsonBuilder)
    {
        this.PROJECT_DIRECTORY_NAME = projectDirectoryName;
        this.gson = gsonBuilder.create();
        this.osUtils = new OSUtils();
        this.DEFAULT_WINDOWS_PATH = Paths.get(System.getenv("APPDATA"), PROJECT_DIRECTORY_NAME).toString();
        this.DEFAULT_MAC_PATH = Paths.get(File.separator, "usr", "local", PROJECT_DIRECTORY_NAME).toString();
        this.DEFAULT_LINUX_PATH = Paths.get(File.separator, "etc", PROJECT_DIRECTORY_NAME).toString();
        this.DEFAULT_UNKNOWN_PATH = DEFAULT_LINUX_PATH;
    }

    private static GsonBuilder defaultGsonBuilder()
    {
        return new GsonBuilder().setPrettyPrinting();
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
     * @throws IOException Thrown if an error occurs during the process
     */
    public <T> T deserialize(String fileName, Class<T> targetClass, SerializeMode serializeMode) throws IOException
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
     * @throws IOException Thrown if an error occurs during the process
     */
    public <T> T deserialize(Path path, Class<T> targetClass, SerializeMode serializeMode) throws IOException
    {
        if (!Files.exists(path)) throw new IllegalArgumentException("No file exists at " + path.toString());
        if (Files.isDirectory(path)) throw new IllegalArgumentException(path.toString() + " is a directory");

        switch (serializeMode)
        {
            case OBJECT:

                try
                {
                    FileInputStream fin = new FileInputStream(path.toFile());
                    ObjectInputStream ois = new ObjectInputStream(fin);
                    T resultObject = targetClass.cast(ois.readObject());

                    ois.close();

                    return resultObject;
                }
                catch (ClassNotFoundException cfe)
                {
                    throw new IOException("Class '" + targetClass.getCanonicalName() + "' was not found during run-time. Was it present during compile-time, though? Perhaps. We may never truly know...");
                }

            case JSON:

                return gson.fromJson(read(path).toString(), targetClass);
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
     * @throws IOException Thrown if an error occurs during the process
     */
    public Path serialize(String fileName, Object object, SerializeMode serializeMode) throws IOException
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
     * @throws IOException Thrown if an error occurs during the process
     */
    public Path serialize(Path path, Object object, SerializeMode serializeMode) throws IOException
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

                FileOutputStream fout = new FileOutputStream(path.toFile());
                ObjectOutputStream oos = new ObjectOutputStream(fout);

                oos.writeObject(object);

                oos.close();
                fout.close();

                break;

            case JSON:
                write(path, gson.toJson(object), WriteMode.OVERWRITE);

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
     * @throws IOException Thrown if anything goes wrong during the write
     */
    public Path write(String fileName, String string, WriteMode writeMode) throws IOException
    {
        if (fileName == null || fileName.isEmpty())
        {
            throw new IllegalArgumentException("The argument 'fileName' must be valid.");
        }

        if (string == null || string.isEmpty())
        {
            throw new IllegalArgumentException("The argument 'string' must be valid.");
        }

        return write(Paths.get(getBasePath().toString(), fileName), string, writeMode);
    }

    /**
     * Writes a string to the specified file. If the file <i>may</i> not exist, the developer must choose the appropriate {@code WriteMode}, e.g. {@code OVERWRITE}.
     *
     * @param path      The file's path
     * @param string    The string to be written to the file
     * @param writeMode How the write process should be done
     * @return Path object representing the final file's location
     * @throws IOException Thrown if anything goes wrong during the write
     */
    public Path write(Path path, String string, WriteMode writeMode) throws IOException
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

        return Files.write(path, string.getBytes(), openOption);
    }

    /**
     * Reads the contents of the specified file.
     *
     * @param fileName The file's name
     * @return StringBuilder containing all the data of the file
     * @throws IOException Thrown if the file could not be read
     */
    public StringBuilder read(String fileName) throws IOException
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
     * @throws IOException Thrown if the file could not be read
     */
    public StringBuilder read(Path path) throws IOException
    {
        StringBuilder stringBuilder = new StringBuilder();
        Files.readAllLines(path).stream().forEach(stringBuilder::append);

        return stringBuilder;
    }

    /**
     * Deletes the specified file or folder in the filesystem.
     *
     * @param fileName   The file's (or folder's) name
     * @param deleteMode Determines how the delete procedure should be executed; e.g. mode {@code RECURSIVE} must be used to delete folders
     * @return true if the delete operation executed, otherwise false
     * @throws IOException Thrown is the file could not be deleted
     */
    public boolean delete(String fileName, DeleteMode deleteMode) throws IOException
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
     * @throws IOException Thrown is the file could not be deleted
     */
    public boolean delete(Path path, DeleteMode deleteMode) throws IOException
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

    /**
     * Creates a new directory in the {@code temp} directory of the application, with an {@code UUID} as the name.
     *
     * @return Path object representing the newly created directory
     * @throws IOException Thrown if the directory could not be created
     */
    public Path newTempDir() throws IOException
    {
        return Files.createDirectory(Paths.get(getTemporaryDirectoryPath().toString(), UUID.randomUUID().toString()));
    }

    /**
     * Creates a new file in the {@code temp} directory of the application, with an {@code UUID} as the name, and {@code tmp} as the file extension.
     *
     * @return Path object representing the newly created file
     * @throws IOException Thrown if the file could not be created
     */
    public Path newTempFile() throws IOException
    {
        return newTempFile("tmp");
    }

    /**
     * Creates a new file in the {@code temp} directory of the application, with an {@code UUID} as the name.
     *
     * @param extension The file extension to be used
     * @return Path object representing the newly created file
     * @throws IOException Thrown if the file could not be created
     */
    public Path newTempFile(String extension) throws IOException
    {
        if (extension == null || extension.isEmpty())
        {
            throw new IllegalArgumentException("The argument 'extension' must be valid.");
        }
        else if (extension.length() >= (255 - 36))
        {
            throw new IllegalArgumentException("The argument 'extension' must less than " + (255 - 36) + " characters long.");
        }

        return Files.createFile(Paths.get(getTemporaryDirectoryPath().toString(), UUID.randomUUID().toString() + "." + extension));
    }

    /**
     * Spawns a new file. Use it for whatever you want, mate.
     *
     * @param fileName The file's name
     * @return Path object representing the newly created file
     * @throws IOException Thrown if the file could not be created
     */
    public Path newFile(String fileName) throws IOException
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
     * @throws IOException Thrown if the file could not be created
     */
    public Path newFile(Path path) throws IOException
    {
        Files.createDirectories(path);
        return Files.createFile(path);
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
     * @throws IOException Thrown if the files could not be cleared
     */
    public boolean clearTempFiles() throws IOException
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

