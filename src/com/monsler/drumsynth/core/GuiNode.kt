package com.monsler.drumsynth.core

import com.monsler.drumsynth.Main
import java.awt.*
import java.awt.event.MouseAdapter
import java.awt.event.MouseEvent
import java.awt.event.MouseMotionAdapter
import javax.swing.ImageIcon
import javax.swing.JLabel
import javax.swing.JPanel


class GuiNode(description: String, functionalFile: String) : JPanel() {
    val lbl = JLabel(description)
    val audio = AudioNode(functionalFile)
    private var dragging = false
    private var initLoc = Point(0, 0)
    private var initLocSelf = Point(0, 0)
    private var completed = false
    var delbtn = JLabel()

    init {
        background = Color(0, 0, 0, 0)
        layout = null

        lbl.setBounds(3, 0, width-3, height)
        lbl.foreground = Color.WHITE
        delbtn.icon = ImageIcon(ImageIcon(Main::class.java.getResource("trash.png")).image.getScaledInstance(15, 15, Image.SCALE_SMOOTH))
        add(delbtn)
        add(lbl)
        delbtn.setBounds(33, 2, 15, 15)
        addMouseListener(object : MouseAdapter() {
            override fun mousePressed(e: MouseEvent) {
                initLoc = e.locationOnScreen
                initLocSelf = location
            }
        })

        addMouseMotionListener(object : MouseMotionAdapter() {
            override fun mouseDragged(e: MouseEvent) {
                val finalMouseLocation = e.locationOnScreen

                val deltaX: Int = finalMouseLocation.x - initLoc.x

                val newLocation = Point(

                    initLocSelf.x + deltaX,

                    0

                )

                location = newLocation
            }
        })
    }

    fun invoke() {
        if (!completed) {
            Thread {
                audio.play()
            }.start()
            completed = true
        }
    }

    fun reset() {
        completed = false
    }

    override fun paintComponent(g: Graphics) {
        val g2d = g.create() as Graphics2D
        lbl.setBounds(3, 0, width-3, height)
        g2d.setRenderingHint(
            RenderingHints.KEY_ANTIALIASING,
            RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setRenderingHint(
            RenderingHints.KEY_TEXT_ANTIALIASING,
            RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        g2d.color = background
        g2d.fillRoundRect(0, 0, width, height, 5, 5)
        g2d.color = background.darker()
        g2d.fillRoundRect(2, 2, width-4, height-4, 5, 5)


        repaint()
    }
}