package com.monsler.drumsynth

import com.formdev.flatlaf.FlatDarkLaf
import com.formdev.flatlaf.FlatLightLaf
import com.jthemedetecor.OsThemeDetector
import com.monsler.drumsynth.layouts.Base
import java.awt.Dimension
import java.awt.Image
import javax.swing.ImageIcon
import javax.swing.JFrame
import javax.swing.SwingUtilities

class Main {
    companion object {
        lateinit var frame: JFrame
    }

}


fun main() {

    val detector = OsThemeDetector.getDetector();
    if (detector.isDark) {
        FlatDarkLaf.setup()
    } else {
        FlatLightLaf.setup()
    }
    val handle = JFrame("DrumSynth")
    handle.iconImage = ImageIcon(Main::class.java.getResource("drum-set.png")).image.getScaledInstance(32, 32, Image.SCALE_SMOOTH)

    Main.frame = handle
    handle.add(Base())
    handle.size = Dimension(400, 250)
    handle.setLocationRelativeTo(null)
    handle.isVisible = true
    handle.defaultCloseOperation = JFrame.EXIT_ON_CLOSE
    handle.isResizable = false

    detector.registerListener { isDark ->
        SwingUtilities.invokeLater {
            if (isDark) {
                FlatDarkLaf.setup()
                handle.repaint()
            } else {
                FlatLightLaf.setup()
                handle.repaint()
            }
        }

    }
}