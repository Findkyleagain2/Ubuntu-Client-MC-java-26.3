import tkinter as tk
from tkinter import messagebox
import socket

class UbuntuUI:
    def __init__(self):
        self.root = tk.Tk()
        self.root.title("Ubuntu Client Controller")
        self.root.configure(bg="#1a1a1a")
        self.root.geometry("300x400")

        self.style_color = "#FF8C00"

        tk.Label(self.root, text="Ubuntu Client", fg=self.style_color, bg="#1a1a1a", font=("Arial", 16)).pack(pady=10)

        tk.Button(self.root, text="Toggle SilentAim", command=lambda: self.send_cmd("AIM"), bg="#333", fg=self.style_color).pack(fill='x', padx=20)
        tk.Button(self.root, text="Toggle Velocity", command=lambda: self.send_cmd("VEL"), bg="#333", fg=self.style_color).pack(fill='x', padx=20)

        self.root.mainloop()

    def send_cmd(self, cmd):
        try:
            with socket.socket(socket.AF_INET, socket.SOCK_STREAM) as s:
                s.connect(('127.0.0.1', 9999))
                s.sendall(cmd.encode())
        except Exception as e:
            print(f"Connection Error: {e}")


if __name__ == "__main__":
    UbuntuUI()
