package application.sephirmobile.notifier;

import android.content.Context;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

public class Persister {
    private Context context;

    public Persister(Context context) {
        this.context = context;
    }

    public void persist(String fileName, Object object) throws IOException {
        try (FileOutputStream fileOutputStream = context.openFileOutput(fileName, Context.MODE_PRIVATE);
             ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream)) {
            objectOutputStream.writeObject(object);
        }
    }

    public Object load(String fileName) throws IOException, ClassNotFoundException {
        try (FileInputStream fileInputStream = context.openFileInput(fileName);
             ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream)) {
            return objectInputStream.readObject();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }
}
