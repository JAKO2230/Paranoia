package View;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;

public class DialogoPuzzle extends JDialog {

    private JTextField campoRespuesta;
    private String respuesta;

    public DialogoPuzzle(Frame padre, String pregunta) {
        super(padre, "Puzzle del cofre", true);
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        setResizable(false);
        respuesta = null;

        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        JLabel titulo = new JLabel("Resuelve el puzzle para abrir el cofre:");
        titulo.setFont(new Font("Arial", Font.BOLD, 15));
        panel.add(titulo, BorderLayout.NORTH);

        JTextArea areaPregunta = new JTextArea(pregunta);
        areaPregunta.setEditable(false);
        areaPregunta.setLineWrap(true);
        areaPregunta.setWrapStyleWord(true);
        areaPregunta.setOpaque(false);
        areaPregunta.setFont(new Font("Arial", Font.PLAIN, 16));
        panel.add(areaPregunta, BorderLayout.CENTER);

        JPanel panelInferior = new JPanel(new BorderLayout(5, 5));
        campoRespuesta = new JTextField();
        campoRespuesta.setFont(new Font("Arial", Font.PLAIN, 16));
        panelInferior.add(campoRespuesta, BorderLayout.NORTH);

        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton botonResponder = new JButton("Responder");
        JButton botonCancelar = new JButton("Cancelar");

        botonResponder.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                respuesta = campoRespuesta.getText();
                dispose();
            }
        });

        botonCancelar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                respuesta = null;
                dispose();
            }
        });

        panelBotones.add(botonResponder);
        panelBotones.add(botonCancelar);
        panelInferior.add(panelBotones, BorderLayout.SOUTH);

        panel.add(panelInferior, BorderLayout.SOUTH);
        add(panel);
        getRootPane().setDefaultButton(botonResponder);

        setSize(440, 260);
        setLocationRelativeTo(padre);

        addWindowListener(new WindowAdapter() {
            public void windowOpened(WindowEvent e) {
                campoRespuesta.requestFocusInWindow();
            }
        });
    }

    public String mostrarDialogo() {
        setVisible(true);
        return respuesta;
    }
}
