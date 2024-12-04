from PySide6.QtWidgets import QApplication, QWidget
from activity import Activity
from PySide6.QtGui import QIcon, QWheelEvent
from PySide6.QtCore import QPoint
import os, threading

def load_file(path: str) -> str:
    return os.path.join(os.path.dirname(__file__), path)

class MainWindow(QWidget):
    mpos: QPoint

    def __init__(self):
        super().__init__()
        self.setWindowIcon(QIcon(load_file('res/drum-set.png')))
        self.setWindowTitle('DrumSynth')
        self.setFixedSize(400, 215)
        self.activity = Activity(self)
    
    def mouseMoveEvent(self, event):
        pass
       
    
    def wheelEvent(self, event: QWheelEvent):
        self.mpos = event.position()
        if not self.activity.playing:
            self.activity.scroll_x = event.angleDelta().y()/10
            self.activity.scrolled_x += self.activity.scroll_x

            self.activity.string1.move(self.activity.string1.x()+self.activity.scroll_x, 0)
            self.activity.string2.move(self.activity.string2.x()+self.activity.scroll_x, 0)
            self.activity.string3.move(self.activity.string3.x()+self.activity.scroll_x, 0)
            for elem in self.activity.array_1:
                elem.move(elem.x()+event.angleDelta().y()/10, 0)
            for elem in self.activity.array_2:
                elem.move(elem.x()+event.angleDelta().y()/10, 0)
            for elem in self.activity.array_3:
                elem.move(elem.x()+event.angleDelta().y()/10, 0)

if __name__ == '__main__':
    app = QApplication()

    handle = MainWindow()
    handle.show()
    
    app.exec()