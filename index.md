### Android-InLineColorPicker 1.1
A simple inline color picker for android.

### Usage
For Android Studio, just add "inlinecolorpicker" folder into your project as gradle project

widget
```markdown
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
        app:space="5dp"
        />
```
java

```markdown
    InLineColorPicker inLineColorPicker = (InLineColorPicker) findViewById(R.id.inline);

    inLineColorPicker.setColors(new int[]{Color.RED, Color.GREEN});
    inLineColorPicker.setOnColorChangeListener(new InLineColorPicker.OnColorChangeListener() {
      @Override
      public void onColorChange(int color, String hex) {
        System.out.println("color: "+color+" - hex string: "+hex);
      }
    });

    inLineColorPicker.setSpace(20);
    inLineColorPicker.setBorderWidth(5); // setting borderWidth will cause to show selected color by border
    inLineColorPicker.setBorderColor(Color.RED); // for default the library will use of selected item color
    inLineColorPicker.setRadius(40);

    System.out.println("----------: "+inLineColorPicker.getSelectedColorHex());
```
