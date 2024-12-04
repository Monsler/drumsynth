from PySide6.QtWidgets import QWidget, QPushButton, QLabel, QFrame, QComboBox, QLineEdit, QGraphicsOpacityEffect
from PySide6.QtGui import QPixmap, QPalette, QColor, QPainter, QPen, QMouseEvent
from PySide6.QtCore import QSize, Qt, QRect, QPoint, Signal, QPropertyAnimation, QEasingCurve
import time, threading
from playsound import playsound
import os
from temper import Temper

class ClickedLabel(QLabel):
    clicked = Signal()

    def mouseReleaseEvent(self, e: QMouseEvent):
        super().mouseReleaseEvent(e)
        self.clicked.emit()

def load_file(path: str) -> str:
    return os.path.join(os.path.dirname(__file__), path)


class Sound:
    def __init__(self, audio_path: str):
        self._pth = audio_path
        self._completed = False
    
    def play(self):
        threading.Thread(target=lambda: playsound(self._pth), daemon=True).start()
        
        self._completed = True
    
    def reset(self):
        self._completed = False
    

class ElementSegment(QFrame):
    def __init__(self, parent: QWidget, label: str, sound_path: str):
        super().__init__(parent)
        self.setGeometry(0, 0, 50, 30)
        self.lbl = QLabel(label, self)
        self.lbl.setGeometry(3, 0, 50, 30)
        self.pal = QPalette()
        if os.path.exists(sound_path):
            self.sound = Sound(sound_path)
        else:
            raise OSError(f'{sound_path} doesn\'t exists')
        self.pal.setColor(QPalette.ColorRole.Window, QColor('#48a832'))
        self.delete = ClickedLabel(self)
        ic = QPixmap(load_file('res/trash.png')).scaled(16, 16, Qt.AspectRatioMode.IgnoreAspectRatio, Qt.TransformationMode.SmoothTransformation)
        self.delete.move(34, 0)
        self.delete.setPixmap(ic)
        del ic
        self.setPalette(self.pal)
        self.dragging = False
        self.drag_start_position = QPoint()
        self.frame_start_position = QPoint()
    
    def _finished(self):
        self.hide()
        self.destroy()

    def anim(self):
        self._finished()
        
    
    def mousePressEvent(self, event):
        if event.button() == Qt.MouseButton.LeftButton:
            self.dragging = True
            self.drag_start_position = event.globalPosition().toPoint()  # Сохраняем глобальную позицию мыши
            self.frame_start_position = self.pos()  # Сохраняем начальную позицию сегмента

    def mouseMoveEvent(self, event):
        if self.dragging:
            delta = event.globalPosition().toPoint() - self.drag_start_position
            new_x = self.frame_start_position.x() + delta.x()
            new_x = round(new_x / 5) * 5
            self.move(new_x, self.y())

    def mouseReleaseEvent(self, event):
        if event.button() == Qt.MouseButton.LeftButton:
            self.dragging = False
    
    def paintEvent(self, _):
        p = QPainter(self)
        pen = QPen(QColor('#48a832').darker(), 3)
        pen.setJoinStyle(Qt.PenJoinStyle.RoundJoin)
        pen.setCapStyle(Qt.PenCapStyle.RoundCap)
        p.setBrush(QColor('#48a832'))
        p.setRenderHint(QPainter.RenderHint.Antialiasing)
        p.setPen(pen)
        
        p.drawRoundedRect(QRect(0, 0, self.width(), self.height()), 7, 7)
        

class Frame(QFrame):
    clicked = Signal()
    right_clicked = Signal()
    mouse: QPoint

    def mousePressEvent(self, event: QMouseEvent):
        self.mouse = event.position()
        if event.button() == Qt.MouseButton.LeftButton:
            self.clicked.emit()
        elif event.button() == Qt.MouseButton.RightButton:
            self.right_clicked.emit()

class Activity():
    scroll_x = 0
    scrolled_x = 0
    array_1: list[ElementSegment] = []
    array_2: list[ElementSegment] = []
    array_3: list[ElementSegment] = []
    current_road = 1

    def __init__(self, parent: QWidget):
        self.bg = QLabel(parent)
        self.playing = False
        pal = QPalette()
        pal.setColor(QPalette.ColorRole.Window, QColor(0, 0, 0))
        img = QPixmap(load_file('res/bg.jpeg')).scaled(QSize(400, 250), Qt.AspectRatioMode.IgnoreAspectRatio, Qt.TransformationMode.SmoothTransformation)
        self.bg.setPixmap(img)
        self.s1 = Frame(parent)
        self.s1.setGeometry(10, 10, parent.width()-20, 30)
        self.s1.setAutoFillBackground(True)
        self.s2 = Frame(parent)
        self.s2.setGeometry(10, 50, parent.width()-20, 30)
        self.s2.setAutoFillBackground(True)
        self.s3 = Frame(parent)
        self.s3.setGeometry(10, 90, parent.width()-20, 30)
        self.s3.setAutoFillBackground(True)
        self.playButton = QPushButton('Play', parent=parent)
        self.playButton.setGeometry(10, 175, 80, 30)
        self.addButton = QPushButton('Add', parent=parent)
        self.addButton.setGeometry(180, 175, 80, 30)
        self.selection = QComboBox(parent)
        self.selection.setGeometry(95, 175, 80, 30)
        self.tempo = QLineEdit(parent)
        self.tempo.setGeometry(10, 140, 160, 30)
        self.tempo.setText('120')
        self.tempo.setPlaceholderText('Tempo')

        self.selection.addItem('Clap')
        self.selection.addItem('Gun')
        self.selection.addItem('Drum1')
        self.selection.addItem('Drum2')
        self.selection.addItem('Drum3')
        self.selection.addItem('Tom1')
        self.selection.addItem('Tom2')
        self.selection.addItem('Tom3')
        self.selection.addItem('Ride')
        self.selection.addItem('Cowbell')
        self.selection.addItem('TimblL')
        self.selection.addItem('TimblH')
        self.selection.addItem('TimpN')
        self.selection.addItem('Crash')
        self.selection.addItem('Hat')
        self.selection.addItem('Prc1')
        self.selection.addItem('Prc2')
        self.selection.addItem('OHat')
        self.selection.addItem('BDrum1')
        self.selection.addItem('BDrum2')
        self.selection.addItem('BDrum3')
        self.selection.addItem('AgoHI')
        self.selection.addItem('AgoLO')
        self.playButton.pressed.connect(self.play)
        self.string1 = QFrame(self.s1)
        self.string1.setAutoFillBackground(True)
        self.string1.setGeometry(0, 0, 2, 30)
        self.string1.setPalette(pal)
        self.string2 = QFrame(self.s2)
        self.string2.setAutoFillBackground(True)
        self.string2.setGeometry(0, 0, 2, 30)
        self.string2.setPalette(pal)
        self.string3 = QFrame(self.s3)
        self.string3.setAutoFillBackground(True)
        self.string3.setGeometry(0, 0, 2, 30)
        self.string3.setPalette(pal)
        self.addButton.pressed.connect(self.adds)
        self.par = parent
        self.s1.clicked.connect(self.r1)
        self.s2.clicked.connect(self.r2)
        self.s3.clicked.connect(self.r3)
        self.s1.right_clicked.connect(self.smp)
        self.s2.right_clicked.connect(self.smp)
        self.s3.right_clicked.connect(self.smp)
        self.repaintBtn = QPushButton('Fix roads', self.par)
        self.repaintBtn.setGeometry(180, 140, 80, 30)
        self.repaintBtn.clicked.connect(self.repaint)
        del pal
    
    def repaint(self):
        self.s1.repaint()
        self.s2.repaint()
        self.s3.repaint()
        self.string1.repaint()
        self.string2.repaint()
        self.string3.repaint()

    def smp(self):
        self.string1.move(self.par.mpos.x(), 0)
        self.string2.move(self.par.mpos.x(), 0)
        self.string3.move(self.par.mpos.x(), 0)
        self.scrolled_x = self.par.mpos.x()

    def r1(self):
        self.current_road = 1
    
    def r2(self):
        self.current_road = 2
    
    def r3(self):
        self.current_road = 3
        
    
    def adds(self):
        if self.current_road == 1:
            p1 = ElementSegment(self.s1, self.selection.currentText(), f'drumset/{self.selection.currentText().lower()}.mp3')
            p1.show()
            self.array_1.append(p1)
            threading.Thread(target=lambda: p1.sound.play(), daemon=True).start()
            p1.delete.clicked.connect(lambda: {
                p1.anim(),
                self.array_1.remove(p1),
                self.s1.repaint()
            })

        if self.current_road == 2:
            p1 = ElementSegment(self.s2, self.selection.currentText(), f'drumset/{self.selection.currentText().lower()}.mp3')
            p1.show()
            self.array_2.append(p1)
            threading.Thread(target=lambda: p1.sound.play(), daemon=True).start()
            p1.delete.clicked.connect(lambda: {
                p1.anim(),
                self.array_2.remove(p1),
                self.s1.repaint()
            })

        if self.current_road == 3:
            p1 = ElementSegment(self.s3, self.selection.currentText(), f'drumset/{self.selection.currentText().lower()}.mp3')
            p1.show()
            self.array_3.append(p1)
            threading.Thread(target=lambda: p1.sound.play(), daemon=True).start()
            p1.delete.clicked.connect(lambda: {
                p1.anim(),
                self.array_3.remove(p1),
                self.s1.repaint()
            })



    def play(self):
        if not self.playing:
            self.playing = True
            self.playButton.setText('Stop')
            self.string1.raise_()
            self.string2.raise_()
            self.string3.raise_()
            threading.Thread(target=_mv, args=(self,), daemon=True).start()
        else:
            global x
            x = self.scrolled_x
            self.playing = False
            self.playButton.setText('Play')
            self.string1.move(self.scrolled_x, 0)
            self.string2.move(self.scrolled_x, 0)
            self.string3.move(self.scrolled_x, 0)
            self.string1.repaint()
            self.string2.repaint()
            self.string3.repaint()
        
global x

def _mv(act: Activity):
    global x
    x = act.scrolled_x
    
    for elem in act.array_1:
            elem.sound.reset()

    for elem in act.array_2:
            elem.sound.reset()

    for elem in act.array_3:
            elem.sound.reset()
    threading.Thread(target=check, args=(act,), daemon=True).start()
    while act.playing:
        time.sleep(Temper.convert(int(act.tempo.text() if act.tempo.text() != '' else '120'), 1)/4000)
        act.string1.move(x, 0)
        act.string2.move(x, 0)
        act.string3.move(x, 0)
        x += 1
        

def check(act: Activity):
    global x
    while act.playing:
        for elem in act.array_1:
            if elem.geometry().intersected(act.string1.geometry()):
                if not elem.sound._completed:
                    elem.sound._completed = True
                    elem.sound.play()
        
        for elem in act.array_2:
            if elem.geometry().intersected(act.string2.geometry()):
                if not elem.sound._completed:
                    elem.sound._completed = True
                    elem.sound.play()
        
        for elem in act.array_3:
            if elem.geometry().intersected(act.string3.geometry()):
                if not elem.sound._completed:
                    elem.sound._completed = True
                    elem.sound.play()

                
                
        