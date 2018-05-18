from cell import *

class Logger:
	def __init__(self):
		self.lines = []
	def log(self, event):
		if isinstance(event, Item):
			self.lines.append("hero uses {}".format(self.item_to_string(event)))
		else:	
			self.lines.append(event)
	def exit(self):
		with open("log.txt", 'w') as otp:
			otp.write('\n'.join(self.lines))


	def item_to_string(self, item):
		if isinstance(item, Wings):
			return "wings"
		if isinstance(item, Quadrocopter):
			return "quadrocopter"