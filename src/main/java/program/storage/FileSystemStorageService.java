package program.storage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.FileSystemUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Base64;
import java.util.UUID;
import java.util.stream.Stream;

@Service
public class FileSystemStorageService implements StorageService {

    private final Path rootLocation; // адреса папки

    @Autowired
    public FileSystemStorageService(StorageProperties properties) {
        this.rootLocation = Paths.get(properties.getLocation());
    }

    @Override
    public void store(MultipartFile file) {
        try {
            if (file.isEmpty()) {
                throw new StorageException("Failed to store empty file " + file.getOriginalFilename());
            }
            Files.copy(file.getInputStream(), this.rootLocation.resolve(file.getOriginalFilename())); // копіює файл в папку
        } catch (IOException e) {
            throw new StorageException("Failed to store file " + file.getOriginalFilename(), e);
        }
    }

    @Override
    public Stream<Path> loadAll() { // Загрузити всі файли з папки
        try {
            return Files.walk(this.rootLocation, 1)
                    .filter(path -> !path.equals(this.rootLocation))
                    .map(path -> this.rootLocation.relativize(path));
        } catch (IOException e) {
            throw new StorageException("Failed to read stored files", e);
        }

    }

    @Override
    public Path load(String filename) {
        return rootLocation.resolve(filename);
    } // загрузити файл з папки

    @Override
    public String store(String base64) { // конвертує base64 в байти і записує в папку
        try {
            if (base64.isEmpty()) {
                throw new StorageException("Failed to store empty base64 ");
            }
            UUID uuid = UUID.randomUUID();
            String randomFileName = uuid.toString()+".jpg";
            String [] charArray = base64.split(",");
            java.util.Base64.Decoder decoder = Base64.getDecoder();
            byte[] bytes = new byte[0];
            bytes = decoder.decode(charArray[1]);
            String directory= "uploaded/"+randomFileName;
            new FileOutputStream(directory).write(bytes);
            return randomFileName;
        } catch (IOException e) {
            throw new StorageException("Failed to store file ", e);
        }

    }

    @Override
    public Resource loadAsResource(String filename) { // зчитати файл по імені
        try {
            Path file = load(filename);
            Resource resource = new UrlResource(file.toUri());
            if(resource.exists() || resource.isReadable()) {
                return resource;
            }
            else {
                throw new StorageFileNotFoundException("Could not read file: " + filename);

            }
        } catch (MalformedURLException e) {
            throw new StorageFileNotFoundException("Could not read file: " + filename, e);
        }
    }

    @Override
    public void deleteAll() {
        FileSystemUtils.deleteRecursively(rootLocation.toFile());
    } // видалити всі файли з папки

    @Override
    public void init() { // створення папки якщо її немає
        try {
            if(!Files.exists(rootLocation))
            {
                Files.createDirectory(rootLocation);
            }
        } catch (IOException e) {
            throw new StorageException("Could not initialize storage", e);
        }
    }
}
