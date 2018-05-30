import logging

'''part of the model'''
'''Represents an element of the game field. Abstract.'''
class Cell:
	def __init__(self):
		raise Exception("Class Cell is abstract")
	def interact(self, game_object):
		if not isinstance(game_object, Cell):
			raise Exception("interaction with non-game object")
		return self, game_object

'''Abstract class representing player character'''
class Hero(Cell):
	default_movepoints = 30
	default_speed = 1
	def __init__(self, movepoints=default_movepoints, vehicle=None):
		raise Exception("Class Hero is abstract")

'''Particular player character. This is the basic one, without special abilities''' 
class Girl(Hero):
	def __init__(self, movepoints=Hero.default_movepoints, speed=Hero.default_speed, vehicle=None):
		self.movepoints = movepoints
		self.inventory = []
		self.vehicle = vehicle
		self.speed = speed

	def interact(self, game_object):
		if not isinstance(game_object, Cell):
			raise Exception("interaction with non-game object")
		if isinstance(game_object, Cloud):
			logging.info("attempt to move into Cloud, nothing happens")
			return self, game_object
		if isinstance(game_object, ThunderCloud):
			logging.info("attempt to move into ThunderCloud, hero loses {} movepoints".format(game_object.power))
			self.movepoints -= game_object.power
			return self, game_object
		if isinstance(game_object, Item):
			logging.info("hero finds {} and adds it to the inventory".format(game_object))
			self.move()
			self.inventory.append(game_object)
			return Empty(), self
		if isinstance(game_object, Bird):
			logging.info("hero is attacked by a Bird and loses and {} additional movepoints"\
				.format(game_object.power))
			self.move()
			self.movepoints -= game_object.power
			return Empty(), self
		if isinstance(game_object, Empty):
			logging.info("hero moves through the air")
			self.move()
			return Empty(), self

	def move(self):
		if not isinstance(self.vehicle, Quadrocopter):
			self.movepoints -= 1
		self.update_vehicle()
	def update_vehicle(self):
		if isinstance(self.vehicle, Wings):
			self.vehicle = None
			self.speed = Hero.default_speed
		elif isinstance(self.vehicle, Quadrocopter):
			self.vehicle.turns_left -= 1
			if self.vehicle.turns_left == 0:
				self.vehicle = None
	def enable_vehicle(self, item):
		self.vehicle = item
		if isinstance(self.vehicle, Wings):
			self.speed = item.bonus_speed
		logging.info("hero uses {}".format(item))
		


'''A "wall" field'''
class Cloud(Cell):
	def __init__(self):
		pass

'''A "wall" field that damages player character'''
class ThunderCloud(Cell):
	default_power = 2
	def __init__(self):
		self.power = ThunderCloud.default_power

'''Just an empty element of the game field'''
class Empty(Cell):
	def __init__(self):
		pass

'''A hostile NPC. Moves randomly, damages player character on clash'''
class Bird(Cell):
	total_birds = 0
	default_power = 5
	def __init__(self):
		self.power = Bird.default_power
		self.id = Bird.total_birds
		Bird.total_birds += 1
	def interact(self, game_object):
		if not isinstance(game_object, Cell):
			raise Exception("interaction with non-game object")
		if isinstance(game_object, Cloud):
			return self, game_object
		if isinstance(game_object, ThunderCloud):
			return self, game_object
		if isinstance(game_object, Item):
			return self, game_object
		if isinstance(game_object, Empty):
			return Empty(), self
		if isinstance(game_object, Bird):
			return self, game_object
		if isinstance(game_object, Hero):
			return game_object.interact(self)
			

'''Abstract class for a cell containing an item'''
class Item(Cell):
	def __init__(self):
		raise Exception("Class Cell is abstract")	
	def __str__(self):
		return "an item"

'''A particular item that grants triple speed for a single turn'''
class Wings(Item):
	default_bonus_speed = 3
	def __init__(self):
		self.bonus_speed = Wings.default_bonus_speed
	def __str__(self):
		return "a wings amulet"

'''A particular item that grants ten free turns'''
class Quadrocopter(Item):
	default_turns = 10
	def __init__(self):
		self.turns_left = Quadrocopter.default_turns
	def __str__(self):
		return "a quadrocopter"