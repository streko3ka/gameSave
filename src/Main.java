import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import java.util.Arrays;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class Main {
    public static void main(String[] args) {
        GameProgress progress1 = new GameProgress(98, 5, 12, 35);
        GameProgress progress2 = new GameProgress(85, 12, 30, 170);
        GameProgress progress3 = new GameProgress(99, 24, 45, 330);
        String save1 = "/Users/admin/Games/GunRunner/savegames/save1.dat";
        String save2 = "/Users/admin/Games/GunRunner/savegames/save2.dat";
        String save3 = "/Users/admin/Games/GunRunner/savegames/save3.dat";
        String zipFile = "/Users/admin/Games/GunRunner/savegames/zip.zip";

        List<String> listSave = Arrays.asList(save1, save2, save3);
        List<GameProgress> listProgress = Arrays.asList(progress1, progress2, progress3);

        saveGame(listSave, listProgress);
        zipFile(listSave, zipFile);
    }

    public static void saveGame(List<String> listSave, List<GameProgress> listProgress) {
        for (int i = 0; i < listSave.size(); i++) {
            for (int j = 0; j < listProgress.size(); j++) {
                if (i == j) {
                    try (FileOutputStream fos = new FileOutputStream(listSave.get(i));
                         ObjectOutputStream oos = new ObjectOutputStream(fos)) {
                        oos.writeObject(listProgress.get(j));
                    } catch (Exception ex) {
                        System.out.println(ex.getMessage());
                    }
                }
            }
        }
    }

    public static void zipFile(List<String> listSave, String zipFile) {
        try (ZipOutputStream zout = new ZipOutputStream(new FileOutputStream(zipFile))) {
            for (String save : listSave) {
                File newFile = new File(save);
                try (FileInputStream fis = new FileInputStream(save)) {
                    ZipEntry entry = new ZipEntry(newFile.getName());
                    zout.putNextEntry(entry);
                    byte[] buffer = new byte[fis.available()];
                    fis.read(buffer);
                    zout.write(buffer);
                    zout.closeEntry();
                } catch (Exception ex) {
                    System.out.println(ex.getMessage());
                }
            }
            System.out.println("Создан архив сохранений");
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        for (int k = 0; k < listSave.size(); k++) {
            File file = new File(listSave.get(k));
            if (file.delete()) {
                System.out.println("Удален лишний файл сохранения " + file.getAbsolutePath());
            } else {
                System.out.println("Ошибка! Не удалось стереть файл");
            }
        }
    }
}