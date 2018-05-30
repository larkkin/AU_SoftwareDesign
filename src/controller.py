import curses
from time import sleep

'''A controller based on curses module. 
   Gets a command from the terminal, returns a string'''
class TerminalController:
    def __init__(self, stdscr):
        self.stdscr = stdscr
        self.key_to_command_dict = {'a' : 'left',
                                    'w' : 'up',
                                    'd' : 'right',
                                    's' : 'down',
                                    'q' : 'exit',
                                    'f' : 'use item',
                                    'r' : 'prev item',
                                    't' : 'next item'}
    def get_key(self):
        return self.stdscr.getkey()
    def key_to_command(self, key):
        key = key.lower()
        if key not in self.key_to_command_dict:
            return "illegal command"
        return self.key_to_command_dict[key.lower()]

    def get_command(self):
        return self.key_to_command(self.get_key())
