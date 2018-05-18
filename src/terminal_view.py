import platform
import os
import model
from cell import *
import curses


class TerminalView:
	cell_to_string = {
		Hero : 'Y',
		Girl : 'Y',
		Cloud : 'O',
		ThunderCloud : '@',
		Empty : ' ',
		Bird : 'v',
		Wings : 'w',
		Quadrocopter : 'x'
	}
	help_message = ["w/a/s/d : movement",
					"r/t : next/previous item",
					"f : use selected item",
					"q : quit"]
	def __init__(self, stdscr):
		self.stdscr = stdscr
	def exit(self):
		curses.endwin()		
	def draw(self, model): #game_map, metainfo, hero_inventory, lost=False, win=False, crash=False):
		if model.crash:
			self.crash(model.metainfo, model.crashinfo)
		elif model.lost:
			self.lose(model.metainfo)
		elif model.end:
			self.end(model.metainfo)
		elif model.win:
			self.win(model.metainfo)
		else:
			self.stdscr.clear()
			self.stdscr.addstr(self.metainfo_str(model.metainfo))
			self.draw_map(model.map)

			inventory_lines = ["\t{}. {}".format(i+1, self.item_to_string(item)) for i, item in  enumerate(model.hero_inventory)]
			if inventory_lines:
				inventory_lines[model.selected_item] += "  <-- selected"
			self.stdscr.addstr("\n\ninventory:\n" +'\n'.join(inventory_lines))
			self.stdscr.refresh()
	def draw_map(self, game_map):
		map_lines = [''.join(map(self.cell_string, row)) for row in game_map[::-1]]
		for i in range(len(TerminalView.help_message)):
			map_lines[3+i] += "\t\t" + TerminalView.help_message[i]
		self.stdscr.addstr('\n'.join(map_lines))
	def cell_string(self, cell):
		return TerminalView.cell_to_string[cell.__class__]
	def metainfo_str(self, metainfo):
		col_length = max((len(key) for key in metainfo))
		return '\n'.join("{} : {}".format(key.ljust(col_length),
										  self.item_to_string(value) if key == 'current vehicle' else value)\
			for key, value in metainfo.items()) + '\n'
	def lose(self, metainfo):
		self.stdscr.clear()
		self.stdscr.addstr("You lost! Now the Girl returns to the real world\nFinal score: {}\n\n(press any key to exit)"\
									.format(metainfo['score']))
		self.stdscr.refresh()
	def win(self, metainfo):
		self.stdscr.clear()
		self.stdscr.addstr("You won! The Girl proceeds to the next sky\nYour current score: {}\n\n(press any key to proceed)"\
									.format(metainfo['score']))
		self.stdscr.refresh()
	def end(self, metainfo):
		self.stdscr.clear()
		self.stdscr.addstr("Congratulations! You have completed the game\n Final score: {}\n\n(press any key to exit)"\
									.format(metainfo['score']))
		self.stdscr.refresh()
	def crash(self, metainfo, crash):
		self.stdscr.clear()
		self.stdscr.addstr("Oops! Something went wrong: {}. Now the Girl returns to the real world\nFinal score: {}\n\n(press any key to exit)"\
									.format(crash, metainfo['score']))
		self.stdscr.refresh()
	def item_to_string(self, item):
		if isinstance(item, Wings):
			return "wings (triple speed for a single turn)"
		if isinstance(item, Quadrocopter):
			return "quadrocopter ({} free turns)".format(item.turns_left)