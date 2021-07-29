package editortexto;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.awt.event.ActionListener;
import java.io.FileReader;
import java.io.IOException;
import java.io.FileNotFoundException;
import java.io.BufferedReader;
import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.PrintWriter;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JFileChooser;
import javax.swing.KeyStroke;
import javax.swing.UIManager;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.JOptionPane;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.DefaultEditorKit;

public class EditorTexto implements ActionListener {

    public JTextArea AreaTxt;
    public JFrame FrameP; //frame principal
    private JMenuBar Menu; //barra de menu principal. 
    private JMenuItem nuevo; //sub menu de archivo.
    private JMenuItem abrir; // sube menu de archivo.
    private JMenuItem guardar; // sub menu de archivo.
    private JMenuItem guardarComo; // sub menu de arhivo.
    private JMenuItem fuente; // sub menu de modificar.
    private JMenuItem integrantes; //sub menu ayuda.
    private JMenu Archivo; // menu de archivo.
    private JMenu Modificar; //medu de modificacion.
    private JMenu Editar; // menu de edicion.
    private JMenu Ayuda; //menu de ayuda (nombre de integrantes,clase etc).
    private JMenuItem salir;// sub menu de archivo.
    private final int FILASTXT = 80;  //filas del JTextArea
    private final int COLUMNASTXT = 80;  //columnas del JtextArea
    private final int LARGOF = 850; //altura del FrameP.
    private final int ANCHOF = 500; //ancho del FrameP.
    private final int ANCHOF2 = 125;//ancho frame secundario.
    private final int LARGOF2 = 225;//altura frame secundario.
    private final int LARGOBTN = 80;//altura del  boton.
    private final int ANCHOBTN = 25;//anchura del boton
    private final int LARGOLBL = 190;//altura del lbl.
    private final int ANCHOLBL = 25;//ancho del lbl.
    private JFileChooser abrirArchivo;
    /* se crea un JFile chooser con el que 
    se crea un cuadro de dialogo para buscar archivos.*/
    private JFileChooser guardarArchivo;
    /* se cre un JFile Chooser con el que 
    se crea un cuadro de dialogo para buscar un direccion donde guardar los
    archivos*/
    private boolean hasChanged = false;//condicion usada para ver si el archivo
    //ha sido modificado
    public JButton siBtn;//boton si del JFrame de salida.
    private JButton noBtn;//boton no del JFrame de salida.
    public JFileChooser guardarArchivoSalir;
    public JButton noBtnNuevo; //boton del frame nuevo del menu archivo.
    private JFrame frame1; //frame del boton nuevo 
    public JButton SiBtnNuevo;// boton si del frame nuevo
    private File archivoActual = null;// condicion para establecer archivo actual
    //(se usa para la accion de guardar).
    private JMenuItem modoLectura;
    private JMenuItem desactivarModoLectura;
    private JMenuItem modificacionTiemporeal;
    private JMenuItem DmodificacionTiempoReal;//desactivar modificacion en tiempo
    //real
    private Timer timer = null; //timer que se utiliza para el modo modificacion en 
    //tiempo real.
    private boolean TimerIniciado = false;

    public static void main(String[] args) {
        EditorTexto editor = new EditorTexto();
    }

    public EditorTexto() {
        try {
            UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows."
                    + "WindowsLookAndFeel");
        } catch (ClassNotFoundException
                | IllegalAccessException
                | InstantiationException
                | UnsupportedLookAndFeelException e) {
            JOptionPane.showMessageDialog(null, "Error al intentar cargar L&F");
        }
        crearEditor(); //funcion que crea los componentes del editor.

    }

    //funcion utilizada para crear visualmente el editor de textos.
    private void crearEditor() {
        FrameP = new JFrame("Editor de textos");//frame donde estara 
        //contenido todo 
        FrameP.setSize(LARGOF, ANCHOF);
        FrameP.setLocationRelativeTo(null);
        FrameP.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);


        /*en esta seccion se crea el JTextArea AreaTxt que es donde se puede
        escribir,se le añade un scroll a este JTextArea para que pueda
        moverse a lo largo del JTextArea,tambien se implementa una funcion que 
        crea el menu superior del editor de texto.
         */
        AreaTxt = new JTextArea(FILASTXT, COLUMNASTXT);

        /*se le asigna un Document listener al area de texto para de esta forma
        poder verificar si se ha producido un cambio, esto funciona en conjutno
        con la funcion setDocumentChanged()*/
        AreaTxt.getDocument().addDocumentListener(new DocumentListener() {

            @Override
            public void insertUpdate(DocumentEvent de) {
                setDocumentChanged(true);
            }

            @Override
            public void removeUpdate(DocumentEvent de) {
                setDocumentChanged(true);
            }

            @Override
            public void changedUpdate(DocumentEvent de) {
                setDocumentChanged(true);
            }

        });
        AreaTxt.setLineWrap(false);
        AreaTxt.setWrapStyleWord(true);

        JScrollPane scroll = new JScrollPane(AreaTxt);
        crearMenu();
        FrameP.getContentPane().add(scroll, BorderLayout.CENTER);
        FrameP.setVisible(true);

    }

    //funcion que crea la barra de menus.
    private void crearMenu() {
        Menu = new JMenuBar();
        Archivo = new JMenu("Archivo");
        Editar = new JMenu("Editar");
        Ayuda = new JMenu("Ayuda");
        Modificar = new JMenu("Modificar");
        nuevo = new JMenuItem("Nuevo");
        abrir = new JMenuItem("Abrir");
        guardar = new JMenuItem("Guardar");
        guardarComo = new JMenuItem("Guardar como");
        integrantes = new JMenuItem("Hecho por:");
        fuente = new JMenuItem("Fuente");
        salir = new JMenuItem("Salir");
        modoLectura = new JMenuItem("Abrir en modo lectura");
        desactivarModoLectura = new JMenuItem("Desactivar modo lectura");
        modificacionTiemporeal = new JMenuItem("Modificacion en tiempo real");
        DmodificacionTiempoReal = new JMenuItem("Desactivar modificacion en "
                + "tiempo real");

        //se añadaden los menus a la barra de menus.
        Menu.add(Archivo);
        Menu.add(Editar);
        Menu.add(Modificar);
        Menu.add(Ayuda);

        // se añaden los sub menus a los menus principales.
        nuevo.addActionListener(this);
        Archivo.add(nuevo);
        Archivo.add(new JSeparator());

        abrir.addActionListener(this);
        Archivo.add(abrir);
        Archivo.add(new JSeparator());

        modoLectura.addActionListener(this);
        Archivo.add(modoLectura);
        Archivo.add(new JSeparator());

        desactivarModoLectura.addActionListener(this);
        Archivo.add(desactivarModoLectura);
        Archivo.add(new JSeparator());

        modificacionTiemporeal.addActionListener(this);
        Archivo.add(modificacionTiemporeal);
        Archivo.add(new JSeparator());

        DmodificacionTiempoReal.addActionListener(this);
        Archivo.add(DmodificacionTiempoReal);
        Archivo.add(new JSeparator());

        guardar.addActionListener(this);
        Archivo.add(guardar);
        Archivo.add(new JSeparator());

        guardarComo.addActionListener(this);
        Archivo.add(guardarComo);
        Archivo.add(new JSeparator());

        salir.addActionListener(this);
        Archivo.add(salir);

        //se añaden las acciones de copiar,pegar y cortar.
        ActionMap acciones = AreaTxt.getActionMap();
        Action accionCopiar = acciones.get(DefaultEditorKit.copyAction);
        Action accionPegar = acciones.get(DefaultEditorKit.pasteAction);
        Action accionCortar = acciones.get(DefaultEditorKit.cutAction);

        accionCopiar.putValue(Action.NAME, "Copiar");
        accionCopiar.putValue(
                Action.ACCELERATOR_KEY,
                KeyStroke.getAWTKeyStroke('C', Event.CTRL_MASK));

        accionCortar.putValue(Action.NAME, "Cortar");
        accionCortar.putValue(
                Action.ACCELERATOR_KEY,
                KeyStroke.getAWTKeyStroke('X', Event.CTRL_MASK));

        accionPegar.putValue(Action.NAME, "Pegar");
        accionPegar.putValue(
                Action.ACCELERATOR_KEY,
                KeyStroke.getAWTKeyStroke('V', Event.CTRL_MASK));

        Editar.add(accionCopiar);
        Editar.add(new JSeparator());
        Editar.add(accionCortar);
        Editar.add(new JSeparator());
        Editar.add(accionPegar);

        Modificar.add(fuente);
        fuente.addActionListener(this);
        Modificar.add(new JSeparator());

        integrantes.addActionListener(this);
        Ayuda.add(integrantes);
        FrameP.getContentPane().add(Menu, BorderLayout.NORTH);

    }


    /*
    funcion que crea un JFrame para asegurarse de que el usuario desea salir del
    programa y le pregunta si quiere guardar lo que hizo.
     */
    public void crearFrameSalida() {
        JFrame frame = new JFrame("salir");
        frame.setSize(LARGOF2, ANCHOF2);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        JPanel panel = new JPanel();
        frame.add(panel);
        panel.setLayout(null);
        JLabel guardarLbl = new JLabel("¿desea guardar su archivo?");
        guardarLbl.setBounds(25, 25, LARGOLBL, ANCHOLBL);
        panel.add(guardarLbl);

        siBtn = new JButton("si");
        siBtn.setBounds(10, 60, LARGOBTN, ANCHOBTN);
        siBtn.addActionListener(
                new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                BotonSi();
            }
        }
        );

        panel.add(siBtn);

        noBtn = new JButton("no");
        noBtn.setBounds(120, 60, LARGOBTN, ANCHOBTN);
        panel.add(noBtn);
        ActionListener BotonSalir = new BotonSalir();
        noBtn.addActionListener(BotonSalir);

        frame.setVisible(true);
    }

    public void crearFrameNuevo() {
        frame1 = new JFrame("guardar");
        frame1.setSize(LARGOF2, ANCHOF2);
        frame1.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame1.setLocationRelativeTo(null);
        JPanel panel = new JPanel();
        frame1.add(panel);
        panel.setLayout(null);
        JLabel guardarLbl = new JLabel("¿desea guardar su archivo?");
        guardarLbl.setBounds(25, 25, LARGOLBL, ANCHOLBL);
        panel.add(guardarLbl);

        SiBtnNuevo = new JButton("si");
        SiBtnNuevo.setBounds(10, 60, LARGOBTN, ANCHOBTN);
        SiBtnNuevo.addActionListener(
                new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                NuevoBotonSi();
            }
        }
        );

        panel.add(SiBtnNuevo);

        noBtnNuevo = new JButton("no");
        noBtnNuevo.setBounds(120, 60, LARGOBTN, ANCHOBTN);
        noBtnNuevo.addActionListener(
                new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent a) {
                BotonNoNuevo();
            }
        }
        );
        panel.add(noBtnNuevo);

        frame1.setVisible(true);
    }

    /*--------------------------------------------------------------------------
              aqui comienza la programacion de los botones del programa.
    --------------------------------------------------------------------------*/

 /*-----------------------------------------------------------------------------
               programacion del JMenuItem abrir
  ----------------------------------------------------------------------------*/
    @Override
    public void actionPerformed(ActionEvent ae) {
        JMenuItem Abrir = (JMenuItem) ae.getSource();
        if (Abrir.getText().equals("Abrir")) {
            accionAbrir();

        }

        //----------------------------------------------------------------------
        //             programacion del JMenuItem guardarComo.
        //----------------------------------------------------------------------
        JMenuItem GuardarComo = (JMenuItem) ae.getSource();
        if (GuardarComo.getText().equals("Guardar como")) {
            GuardarComo();
        }

        /*----------------------------------------------------------------------
                       programacion del JMenuItem salir
------------------------------------------------------------------------------*/
        JMenuItem Salir = (JMenuItem) ae.getSource();
        if (Salir.getText().equals("Salir")) {
            String texto = AreaTxt.getText();
            texto = texto.replaceAll("\n", "");
            texto = texto.replaceAll(" ", "");
            if (documentHasChanged() == false || texto.isEmpty()) {
                System.exit(0);
            } else if (documentHasChanged() == true) {
                crearFrameSalida();

            }

        }
        /*----------------------------------------------------------------------
                     programacion del JMenuItem nuevo
------------------------------------------------------------------------------*/
        JMenuItem Nuevo = (JMenuItem) ae.getSource();
        if (Nuevo.getText().equals("Nuevo")) {
            String texto = AreaTxt.getText();
            texto = texto.replaceAll("\n", "");
            texto = texto.replaceAll(" ", "");
            if (documentHasChanged() == false || texto.isEmpty()) {
                AreaTxt.setText("");
                FrameP.setTitle("Editor de textos");

            } else if (documentHasChanged() == true) {
                crearFrameNuevo();
            }
        }
        /*----------------------------------------------------------------------
                 programacion del JMenuItem guardar.
   -------------------------------------------------------------------------- */
        JMenuItem Guardar = (JMenuItem) ae.getSource();
        if (Guardar.getText().equals("Guardar")) {
            accionGuardar();

        }

        /*----------------------------------------------------------------------
                programacion del JMenuItem modoLectura.
    -------------------------------------------------------------------------*/
        JMenuItem ModoLectura = (JMenuItem) ae.getSource();
        if (ModoLectura.getText().equals("Abrir en modo lectura")) {
            accionAbrir();
            AreaTxt.setEditable(false);

        }
        /*----------------------------------------------------------------------
                programacion del JMenuItem desactivarmodoLectura.
    -------------------------------------------------------------------------*/
        JMenuItem DesactivarModoLectura = (JMenuItem) ae.getSource();
        if (DesactivarModoLectura.getText().equals("Desactivar modo lectura")) {
            AreaTxt.setEditable(true);

        }
        /*----------------------------------------------------------------------
                    programacion del JMenuItem modificacionTiemporeal
    --------------------------------------------------------------------------*/
        JMenuItem ModificacionTiemporeal = (JMenuItem) ae.getSource();
        if (ModificacionTiemporeal.getText().equals("Modificacion en tiempo real"
        )) {
            accionAbrir();
            String texto = AreaTxt.getText();
            texto = texto.replaceAll("\n", "");
            texto = texto.replaceAll(" ", "");
            if (texto.length() != 0) {
                modificacionTiempoReal();
            }

        }
        /*---------------------------------------------------------------------
        programacion del JMenuItem que desactiva la modificacion en tiempo real.
        ----------------------------------------------------------------------*/

        JMenuItem dmodificacionTiempoReal = (JMenuItem) ae.getSource();
        if (dmodificacionTiempoReal.getText().equals("Desactivar modificacion "
                + "en tiempo real")) {
            if (TimerIniciado == true) {
                DesactivarModificacionTiempoReal();
            }
        }
        /*----------------------------------------------------------------------
                         programacion del JMenuItemFuente
         ---------------------------------------------------------------------*/
        JMenuItem Fuente = (JMenuItem) ae.getSource();
        if (Fuente.getText().equals("Fuente")) {
            SeleccionFuente();
        }
        /*----------------------------------------------------------------------
                       programacion del JMenuItem Hecho por:
         ---------------------------------------------------------------------*/
        JMenuItem Hecho = (JMenuItem) ae.getSource();
        if (Hecho.getText().equals("Hecho por:")) {
            JOptionPane.showMessageDialog(null, "Editor de texto Hecho por:"
                    + "Fabian Caceres, Denise Fiallos, Eduardo Salgado, "
                    + "Pogramacion II"
                    + " 2020");
        }
    }

    /*--------------------------------------------------------------------------
    aqui se ecnuentran las funciones que le corresponden a algunos botones .
------------------------------------------------------------------------------*/
    //esta funcion crea la accion del BotonSi del FrameSalir.
    public void BotonSi() {
        String texto = AreaTxt.getText();
        texto = texto.replaceAll("\n", "");
        texto = texto.replaceAll(" ", "");
        if (texto.isEmpty()) {
            JOptionPane.showMessageDialog(null, "no se puede guardar un"
                    + " archivo vacio");
        } else {
            accionGuardar();

        }
        System.exit(0);
    }

    /*
    esta es la funcion que se encarga de cerrar el frame que le pregunta al 
    usuario si desea guardar su archivo al momento de crear uno nuevo.
     */
    public void BotonNoNuevo() {
        frame1.dispose();
        AreaTxt.setText("");
        FrameP.setTitle("Editor de textos");
    }

    private void accionGuardar() {
        if (obtenerArchivoActual() == null) {
            GuardarComo();
        } else if (documentHasChanged() == true) {
            try {
                try (BufferedWriter bw
                        = new BufferedWriter(new FileWriter(obtenerArchivoActual()))) {
                    AreaTxt.write(bw);
                }
                setDocumentChanged(false);
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(null, "no se ha "
                        + "encotrado el archivo");
            }
        }
    }

    private void GuardarComo() {
        String texto = AreaTxt.getText();
        texto = texto.replaceAll("\n", "");
        texto = texto.replaceAll(" ", "");
        if (texto.isEmpty()) {
            JOptionPane.showMessageDialog(null, "no se puede guardar un"
                    + " archivo vacio");
        } else if (documentHasChanged() == true) {
            if (guardarArchivo == null) {
                guardarArchivo = new JFileChooser();
            }

            int seleccion = guardarArchivo.showSaveDialog(FrameP);

            if (seleccion == JFileChooser.APPROVE_OPTION) {
                File f = guardarArchivo.getSelectedFile();
                try {
                    String nombre = f.getName();
                    try (
                            PrintWriter writer = new PrintWriter(f)) {
                        String salto = System.lineSeparator();
                        String towrite = AreaTxt.getText().replace("\n",
                                salto);
                        writer.println(towrite);
                        writer.flush();
                    }
                    establecerArchivoActual(f);
                    setDocumentChanged(false);

                    FrameP.setTitle("Editor de textos: " + nombre);

                } catch (FileNotFoundException exp) {
                }
            }

        }

    }

    public void NuevoBotonSi() {
        String texto = AreaTxt.getText();
        texto = texto.replaceAll("\n", "");
        texto = texto.replaceAll(" ", "");
        if (texto.length() != 0) {
            accionGuardar();
            establecerArchivoActual(null);
            setDocumentChanged(false);
            FrameP.setTitle("Editor de textos");
        }
        frame1.dispose();
        AreaTxt.setText("");
    }

    private void accionAbrir() {
        if (documentHasChanged() == true) {
            int option = JOptionPane.showConfirmDialog(null, "¿Desea "
                    + "guardar los cambios?");
            switch (option) {
                case JOptionPane.YES_OPTION:
                    accionGuardar();
                    break;
                case JOptionPane.CANCEL_OPTION:
                    return;

            }
        }

        if (abrirArchivo == null) {
            abrirArchivo = new JFileChooser();
        }
        /*
            con esto se crea un  filtro para que el editor solo abra archivos
            de texto con extensiones txt,text o text files.
         */
        FileNameExtensionFilter filtro = new FileNameExtensionFilter("T"
                + "EXT FILES", "txt", "text");
        abrirArchivo.setFileFilter(filtro);

        //se limita a que solo se puedan abrir archivos.
        abrirArchivo.setFileSelectionMode(JFileChooser.FILES_ONLY);

        int seleccion = abrirArchivo.showOpenDialog(null);

        if (seleccion == JFileChooser.APPROVE_OPTION) {
            File f = abrirArchivo.getSelectedFile();

            try {
                String nombre = f.getName();
                String direccion = f.getAbsolutePath();
                String contenido = obtenerArchivo(direccion);
                FrameP.setTitle("Editor de textos: " + nombre);

                //se le agrega el contenido del arcivo al TextArea
                AreaTxt.setText(contenido);
                establecerArchivoActual(f); //se establece como el archivo 
                //actual (sirve para la funcion guardar).

                setDocumentChanged(false);

            } catch (Exception exp) {
                JOptionPane.showMessageDialog(null, "no se ha encontrado el "
                        + "archivo");
            }
        }

    }

    //funcion que obtiene el contenido del archvivo que se escogio.
    //(es usada en la funcion abrir).
    private String obtenerArchivo(String ruta) {
        FileReader fr = null;
        BufferedReader br = null;
        //Cadena de texto donde se guarda el contenido del archivo
        String contenido = "";
        try {
            fr = new FileReader(ruta);
            br = new BufferedReader(fr);

            String linea;
            /*
            en caso de haber mas de una linea en el archivo se obtiene la
            informacion linea por linea.
             */
            while ((linea = br.readLine()) != null) {
                contenido += linea + "\n";
            }

        } catch (IOException e) {
        } finally {
            try {
                br.close();
            } catch (IOException ex) {
            }
        }
        return contenido;
    }

    //funcion que implementa el JFontChooser.
    void SeleccionFuente() {
        Font font = JFontChooser.showDialog(FrameP,
                "Editor de Texto - Fuente de letra:",
                AreaTxt.getFont());
        if (font != null) {
            AreaTxt.setFont(font);
        }
    }

    //establece el archivo actual.
    void establecerArchivoActual(File archivoActual) {
        this.archivoActual = archivoActual;
    }

    //obtiene el arhivo actual.
    File obtenerArchivoActual() {
        return archivoActual;
    }

    //retorna si el documento ha cambiado o no 
    boolean documentHasChanged() {
        return hasChanged;
    }

    //establece el estado del documento actual (si ha cambiado o no).
    void setDocumentChanged(boolean hasChanged) {
        this.hasChanged = hasChanged;
    }

    //funcion que se encarga de activar la modificacion en tiempo real.
    void modificacionTiempoReal() {
        //el tiempo esta en milisegundos, por lo tanto se ejecuta cada segundo.
        timer = new Timer(1000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                accionGuardar();
            }
        });
        //el timer indica cada cuanto tiempo se debera ejecutar la accionGuardar.
        timer.start();
        TimerIniciado = true;
    }

    //detiene el timer para desactivar la opcion de guardado cada segundo.
    void DesactivarModificacionTiempoReal() {
        timer.stop();
    }

}
