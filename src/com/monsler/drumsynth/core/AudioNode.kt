package com.monsler.drumsynth.core

import javazoom.jl.player.Player
import java.io.BufferedInputStream
import java.io.FileInputStream

class AudioNode(private val audioPath: String) {

    private val path = BufferedInputStream(FileInputStream(audioPath))
    private var audio: Player = Player(path)

    fun reset() {
        audio.close()
        audio = Player(BufferedInputStream(FileInputStream(audioPath)))
    }

    fun play() {
        Thread {
            audio = Player(BufferedInputStream(FileInputStream(audioPath)))
            audio.play()
        }.start()

    }

    fun getPlayer() : Player {
        return audio
    }

    fun getAudioPath() : String {
        return audioPath
    }
}