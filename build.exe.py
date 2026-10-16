from pathlib import Path

from PyInstaller.__main__ import run

ROOT = Path(__file__).resolve().parent
SCRIPT = ROOT / "gui.py"
OUTPUT_DIR = ROOT / "dist"
WORK_DIR = ROOT / "build" / "pyinstaller"

def build_executable():
    if not SCRIPT.is_file():
        raise FileNotFoundError(f"Python entry point not found: {SCRIPT}")

    run([
        str(SCRIPT),
        "--name=UbuntuClient",
        "--onefile",
        "--windowed",
        f"--distpath={OUTPUT_DIR}",
        f"--workpath={WORK_DIR}",
        f"--specpath={WORK_DIR}",
    ])


if __name__ == "__main__":
    build_executable()