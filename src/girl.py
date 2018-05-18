class Hero:
	default_hitpoints = 100
	default_power = 5

	def __init__(self, hitpoints=default_hitpoints, power=default_power, head, body, main_hand):
		self.hitpoints = hitpoints
		self.power = power

		self.head = head
		self.body = body
		self.main_hand = main_hand


	