package gaj.common.io;

import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.io.Console;
import java.io.IOException;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

public class TextInputter {

    private TextInputter() {}

    public static char/*@Nullable*/[] readHiddenInput(String prompt) throws IOException {
        Console con = System.console();
        return (con != null) ? con.readPassword(prompt) : readHiddenInputDialog(prompt);
    }

    public static char[] readHiddenInputDialog(final String prompt) {
        final JPasswordField jpf = new JPasswordField();
        JOptionPane jop = new JOptionPane(jpf, JOptionPane.QUESTION_MESSAGE, JOptionPane.OK_CANCEL_OPTION);
        JDialog dialog = jop.createDialog(prompt);
        dialog.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentShown(ComponentEvent e) {
                SwingUtilities.invokeLater(new Runnable() {
                    @Override
                    public void run() {
                        jpf.requestFocusInWindow();
                    }
                });
            }
        });
        dialog.setVisible(true);
        int result = (Integer) jop.getValue();
        dialog.dispose();
        return (result == JOptionPane.OK_OPTION) ? jpf.getPassword() : new char[0];
    }

    public static /*@Nullable*/ String readInput(String prompt) throws IOException {
        Console con = System.console();
        return (con != null) ? con.readLine(prompt) : readInputDialog(prompt);
    }

    public static String readInputDialog(final String prompt) {
        final JTextField jf = new JTextField();
        JOptionPane jop = new JOptionPane(jf, JOptionPane.QUESTION_MESSAGE, JOptionPane.OK_CANCEL_OPTION);
        JDialog dialog = jop.createDialog(prompt);
        dialog.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentShown(ComponentEvent e) {
                SwingUtilities.invokeLater(new Runnable() {
                    @Override
                    public void run() {
                        jf.requestFocusInWindow();
                    }
                });
            }
        });
        dialog.setVisible(true);
        int result = (Integer) jop.getValue();
        dialog.dispose();
        String value = (result == JOptionPane.OK_OPTION) ? jf.getText() : null;
        return (value != null) ? value : "";
    }

}
