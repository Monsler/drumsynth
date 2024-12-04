

class Temper:
    @staticmethod
    def convert(bpm: int, desired_hits: int) -> int:
        return desired_hits / (bpm/6000)