package com.monsler.drumsynth.layouts

import com.monsler.drumsynth.core.AudioNode
import com.monsler.drumsynth.core.GuiNode
import com.monsler.drumsynth.core.TempoConverter
import java.awt.*
import java.awt.event.MouseAdapter
import java.awt.event.MouseEvent
import java.awt.event.MouseWheelEvent
import javax.swing.ImageIcon
import javax.swing.JButton
import javax.swing.JComboBox
import javax.swing.JPanel
import javax.swing.JScrollPane
import javax.swing.JTextField

class Base : JPanel() {
    private var img = ImageIcon(this::class.java.getResource("../bg.jpeg")).image
    private val linear1 = JPanel()
    private val linear2 = JPanel()
    private val linear3 = JPanel()
    private val plateColor = Color.decode("#48a832")
    private val string1 = JPanel()
    private val string2 = JPanel()
    private val string3 = JPanel()
    private val playButton = JButton("Play")
    private val addButton = JButton("Add")
    private var plays = false
    private var listStrings1: List<GuiNode> = ArrayList()
    private var listStrings2: List<GuiNode> = ArrayList()
    private var listStrings3: List<GuiNode> = ArrayList()
    private var currentRoad = 1;
    private val listInst = JComboBox<String>()
    private var scrollX = 0;
    private var scrolledX = 0;
    private val tempField = JTextField()


    init {
        listInst.addItem("Clap")
        listInst.addItem("Gun")
        listInst.addItem("Drum1")
        listInst.addItem("Drum2")
        listInst.addItem("Drum3")
        listInst.addItem("Tom1")
        listInst.addItem("Tom2")
        listInst.addItem("Tom3")
        listInst.addItem("Ride")
        listInst.addItem("Cowbell")
        listInst.addItem("TimblL")
        listInst.addItem("TimblH")
        listInst.addItem("TimpN")
        listInst.addItem("Crash")
        listInst.addItem("Hat")
        listInst.addItem("Prc1")
        listInst.addItem("Prc3")
        listInst.addItem("OHat")
        listInst.addItem("BDrum1")
        listInst.addItem("BDrum2")
        listInst.addItem("BDrum3")
        listInst.addItem("AgoHI")
        listInst.addItem("AgoLO")

        img = img.getScaledInstance(385, 250, Image.SCALE_SMOOTH)
        layout = null
        add(linear1)
        add(linear2)
        add(linear3)
        add(playButton)
        add(listInst)
        add(addButton)
        add(tempField)
        linear1.layout = null
        linear3.layout = null
        linear2.layout = null
        linear1.setBounds(10, 10, 365, 30)
        linear2.setBounds(10, 50, 365, 30)
        linear3.setBounds(10, 90, 365, 30)
        linear1.background = Color(0, 0, 0, 0)
        linear2.background = Color(0, 0, 0, 0)
        linear3.background = Color(0, 0, 0, 0)
        linear3.add(string3)
        linear2.add(string2)
        linear1.add(string1)
        string1.setBounds(0, 0, 2, 30)
        string2.setBounds(0, 0, 2, 30)
        string3.setBounds(0, 0, 2, 30)
        listInst.setBounds(195/2, 175, 80, 30)
        string1.background = Color.black
        string2.background = Color.black
        string3.background = Color.black
        playButton.setBounds(10, 175, 80, 30)
        tempField.setBounds(10, 135, 170, 30)
        tempField.text = "70"
        repaint()
        addButton.setBounds(listInst.x+88, 175, 80, 30)
        addButton.addMouseListener(object : MouseAdapter() {
            override fun mouseClicked(e: MouseEvent) {
                val selection = listInst.selectedItem!!.toString()
                val node = GuiNode(selection, "drumset/${selection.lowercase()}.mp3")
                node.background = plateColor
                node.setBounds(2, 0, 50, 30)

                when (currentRoad) {
                    1 -> {
                        linear1.add(node)
                        listStrings1 += node
                        linear1.setComponentZOrder(node, 1)
                        linear1.setComponentZOrder(string1, 1)
                        node.delbtn.addMouseListener(object : MouseAdapter() {
                            override fun mouseClicked(e: MouseEvent) {
                                linear1.remove(node)
                                listStrings1 -= node
                            }
                        })
                    }
                    2 -> {
                        linear2.add(node)
                        listStrings2 += node
                        linear2.setComponentZOrder(node, 1)
                        linear2.setComponentZOrder(string2, 1)
                        node.delbtn.addMouseListener(object : MouseAdapter() {
                            override fun mouseClicked(e: MouseEvent) {
                                linear2.remove(node)
                                listStrings2 -= node
                            }
                        })
                    }
                    3 -> {
                        linear3.add(node)
                        listStrings3 += node
                        linear3.setComponentZOrder(node, 1)
                        linear3.setComponentZOrder(string3, 1)
                        node.delbtn.addMouseListener(object : MouseAdapter() {
                            override fun mouseClicked(e: MouseEvent) {
                                linear3.remove(node)
                                listStrings3 -= node
                            }
                        })
                    }
                }
            }
        })

        linear1.addMouseListener(object : MouseAdapter() {
            override fun mousePressed(e: MouseEvent) {
                currentRoad = 1
                if (e.button == 3) {
                    scrolledX = e.x
                    string1.setLocation(scrolledX, 0)
                    string2.setLocation(scrolledX, 0)
                    string3.setLocation(scrolledX, 0)
                }
            }
        })
        linear2.addMouseListener(object : MouseAdapter() {
            override fun mousePressed(e: MouseEvent) {
                currentRoad = 2
                if (e.button == 3) {
                    scrolledX = e.x
                    string1.setLocation(scrolledX, 0)
                    string2.setLocation(scrolledX, 0)
                    string3.setLocation(scrolledX, 0)
                }
            }
        })
        linear3.addMouseListener(object : MouseAdapter() {
            override fun mousePressed(e: MouseEvent) {
                currentRoad = 3
                if (e.button == 3) {
                    scrolledX = e.x
                    string1.setLocation(scrolledX, 0)
                    string2.setLocation(scrolledX, 0)
                    string3.setLocation(scrolledX, 0)
                }
            }
        })

        playButton.addMouseListener(object : MouseAdapter() {
            override fun mousePressed(e: MouseEvent) {
                if (!plays) {
                    playButton.text = "Stop"
                    plays = true
                    string1.setLocation(scrolledX, 0)
                    string2.setLocation(scrolledX, 0)
                    string3.setLocation(scrolledX, 0)
                    var xPos = scrolledX
                    for (elem in listStrings1) {
                        elem.audio.reset()
                        elem.reset()
                    }
                    for (elem in listStrings3) {
                        elem.audio.reset()
                        elem.reset()
                    }
                    for (elem in listStrings2) {
                        elem.audio.reset()
                        elem.reset()
                    }

                    Thread {
                        while (plays) {
                            Thread.sleep(TempoConverter.calculateIncreaseCoefficient(tempField.text.toInt(), 1).toLong()/4)
                            string1.setLocation(xPos, 0)
                            string2.setLocation(xPos, 0)
                            string3.setLocation(xPos, 0)
                            xPos += 1
                        }
                    }.start()
                    Thread {
                        while (plays) {
                            Thread.sleep(10)
                            for (elem in listStrings1) {
                                if (string1.bounds.intersects(elem.bounds)) {
                                    elem.invoke()
                                }
                            }
                            for (elem in listStrings2) {
                                if (string1.bounds.intersects(elem.bounds)) {
                                    elem.invoke()
                                }
                            }
                            for (elem in listStrings3) {
                                if (string1.bounds.intersects(elem.bounds)) {
                                    elem.invoke()
                                }
                            }
                        }
                    }.start()
                } else {
                    plays = false
                    playButton.text = "Play"
                    string1.setLocation(scrolledX, 0)
                    string2.setLocation(scrolledX, 0)
                    string3.setLocation(scrolledX, 0)
                    string1.repaint()
                    string2.repaint()
                    string3.repaint()
                }
            }
        })

        addMouseWheelListener(object : MouseAdapter() {
            override fun mouseWheelMoved(e: MouseWheelEvent) {
                if(!plays) {
                    scrollX = e.wheelRotation * 15

                    string1.setLocation(string1.x + scrollX, 0)
                    string2.setLocation(string2.x + scrollX, 0)
                    string3.setLocation(string3.x + scrollX, 0)
                    scrolledX += scrollX
                    for (elem in listStrings1) {
                        elem.setLocation(elem.x + scrollX, 0)
                    }
                    for (elem in listStrings2) {
                        elem.setLocation(elem.x + scrollX, 0)
                    }
                    for (elem in listStrings3) {
                        elem.setLocation(elem.x + scrollX, 0)
                    }
                }
            }
        })
    }

    fun complementaryColor(bgColor: Color): Color {
        val complement = Color(
            255 - bgColor.red,
            255 - bgColor.green,
            255 - bgColor.blue
        )

        return complement
    }

    override fun paintComponent(g: Graphics) {
        val g2 = g.create() as Graphics2D
        g2.setRenderingHint(
            RenderingHints.KEY_ANTIALIASING,
            RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setRenderingHint(
            RenderingHints.KEY_TEXT_ANTIALIASING,
            RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        g2.drawImage(img, 0, -20, null)
        g2.color = background
        g2.fillRoundRect(10, 10, 365, 30, 10, 10)
        g2.fillRoundRect(10, 50, 365, 30, 10, 10)
        g2.fillRoundRect(10, 90, 365, 30, 10, 10)
        repaint()
    }
}