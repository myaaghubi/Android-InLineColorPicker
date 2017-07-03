# Android-InLineColorPicker
A simple inline color picker for android.


## Shot

![Alt text](/screenshot/shot1.jpg?raw=true "InLineColorPicker")


## Usage

As widget
```
    <com.myaghobi.inlinecolorpicker.InLineColorPicker
        android:id="@+id/inline"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        app:colors="@array/defaultPaletteColor"
        app:defaultSelectedColor="2"
        app:alignment="center"
        app:borderColor="#ff0000"
        app:borderWidth="5dp"
        app:radius="22dp"
        app:space="3dp"
        />
```

As java
```
    InLineColorPicker inLineColorPicker = (InLineColorPicker) findViewById(R.id.inline);

    inLineColorPicker.setColors(new int[]{Color.RED, Color.GREEN});
    inLineColorPicker.setOnColorChangeListener(new InLineColorPicker.OnColorChangeListener() {
      @Override
      public void onColorChange(int color, String hex) {
        System.out.println("color: "+color+" - hex string: "+hex);
      }
    });

    inLineColorPicker.setSpace(20);
    inLineColorPicker.setBorderWidth(10);
    inLineColorPicker.setRadius(60);

    System.out.println("----------: "+inLineColorPicker.getSelectedColorHex());
```
