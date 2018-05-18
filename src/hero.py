class Hero(Cell):
	default_movepoints = 10

	def __init__(self, movepoints=default_movepoints, vehicle):
		raise Exception("Class Hero is abstract")
		self.movepoints = movepoints
		
		self.back = back
		self.vehicle = vehicle
		self.main_hand = main_hand




class Girl(Hero): 
	self.inventory = []
	def __init__(self, movepoints=default_movepoints, vehicle):
		self.movepoints = movepoints
		
		self.back = back
		self.vehicle = vehicle
		self.main_hand = main_hand

	def interact(game_object):
		if !isinstance(game_object, Cell):
			raise Exception("interaction with non-game object")
		if isinstance(game_object, Cloud):
			return self, game_object
		if isinstance(game_object, ThunderCloud):
			self.movepoints -= 2
			return self, game_object
		if isinstance(game_object, Item):
			self.movepoints -= 
			self.inventory.apend(game_object)
			return Empty(), self
