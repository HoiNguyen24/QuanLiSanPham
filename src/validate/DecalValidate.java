package src.validate;

import src.model.PhotoDecal;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static src.validate.ClothesValidate.scanner;

public class DecalValidate {
    public static boolean checkName(ArrayList<PhotoDecal> photos, String name) {
        for (PhotoDecal photo : photos) {
            if (name.equals(photo.getName()))
                return false;
        }
        return true;

    }

    public static String Name(ArrayList<PhotoDecal> photos) {
        StringBuffer stringBuffer = new StringBuffer();
        do {
            System.out.println("Nhập tên hình: ");
            stringBuffer.delete(0, stringBuffer.length());
            stringBuffer.append(scanner.nextLine());
        } while (!checkName(photos, stringBuffer.toString()));
        return stringBuffer.toString();
    }

    public static boolean checkCode(ArrayList<PhotoDecal> photos, String code) {
        Pattern pattern = Pattern.compile("^[0-9a-fA-F]{6}$");
        Matcher matcher = pattern.matcher(code);
        if (matcher.find()) return false;
        for (PhotoDecal photo : photos) {
            if (code.equals(photo.getCode()))
                return false;
        }
        return true;
    }

    public static String Code(ArrayList<PhotoDecal> photos) {
        StringBuffer stringBuffer = new StringBuffer();
        do {
            System.out.println("Nhập mã hình: ");
            stringBuffer.delete(0, stringBuffer.length());
            stringBuffer.append(scanner.nextLine());
        } while (!checkCode(photos, stringBuffer.toString()));
        return stringBuffer.toString();
    }

    public static long unit(ArrayList<PhotoDecal> photos) {
        long unit;
        while (true) {
            try {
                unit = Long.parseLong(scanner.nextLine());
                break;
            } catch (Exception e) {
                System.out.println("Nhập lại");
            }
        }
        return unit;
    }
}
