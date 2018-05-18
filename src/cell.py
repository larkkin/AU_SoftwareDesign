'''part of the model'''


class Cell:
	def __init__(self):
		raise Exception("Class Cell is abstract")
	def interact(self, game_object, logger):
		if not isinstance(game_object, Cell):
			raise Exception("interaction with non-game object")
		return self, game_object


class Hero(Cell):
	default_movepoints = 30
	default_speed = 1
	def __init__(self, movepoints=default_movepoints, vehicle=None):
		raise Exception("Class Hero is abstract")

class Girl(Hero):
	def __init__(self, movepoints=Hero.default_movepoints, speed=Hero.default_speed, vehicle=None, inventory=[]):
		self.movepoints = movepoints
		self.inventory = inventory
		self.vehicle = vehicle
		self.speed = speed

	def interact(self, game_object, logger):
		if not isinstance(game_object, Cell):
			raise Exception("interaction with non-game object")
		if isinstance(game_object, Cloud):
			logger.log("attend to move into Cloud, nothing happens")
			return self, game_object
		if isinstance(game_object, ThunderCloud):
			logger.log("attend to move into ThunderCloud, hero loses {} movepoints".format(game_object.power))
			# self.move()
			self.movepoints -= game_object.power
			return self, game_object
		if isinstance(game_object, Item):
			logger.log("hero finds {} and adds it to the inventory"\
				.format("wings" if isinstance(game_object, Wings) else "quadrocopter"))
			self.move()
			self.inventory.append(game_object)
			return Empty(), self
		if isinstance(game_object, Bird):
			logger.log("hero is attacked by a Bird and loses and {} additional movepoints"\
				.format(game_object.power))
			self.move()
			self.movepoints -= game_object.power
			return Empty(), self
		if isinstance(game_object, Empty):
			logger.log("hero moves through the air")
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
	def enable_vehicle(self, item, logger):
		self.vehicle = item
		if isinstance(self.vehicle, Wings):
			self.speed = item.bonus_speed
		logger.log(item)
		



class Cloud(Cell):
	def __init__(self):
		pass

class ThunderCloud(Cell):
	default_power = 2
	def __init__(self):
		self.power = ThunderCloud.default_power


class Empty(Cell):
	def __init__(self):
		pass

class Bird(Cell):
	total_birds = 0
	default_power = 5
	def __init__(self):
		self.power = Bird.default_power
		self.id = Bird.total_birds
		Bird.total_birds += 1
	def interact(self, game_object, logger):
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
			return game_object.interact(self, logger)
			


class Item(Cell):
	def __init__(self):
		raise Exception("Class Cell is abstract")	

class Wings(Item):
	default_bonus_speed = 3
	def __init__(self):
		self.bonus_speed = Wings.default_bonus_speed

class Quadrocopter(Item):
	default_turns = 10
	def __init__(self):
		self.turns_left = Quadrocopter.default_turns