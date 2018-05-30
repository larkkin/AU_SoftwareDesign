import unittest
from unittest.mock import MagicMock

from model import *


class TestTerminalView(unittest.TestCase):

    def test_get_map_lines(self):
        scr = None
        view = TerminalView(scr)
        game_map = [[Cloud(), Empty(), Empty(), Cloud()],
               [Cloud(), Empty(), Quadrocopter(), Cloud()],
               [Cloud(), Bird(), Empty(), Cloud()],
               [Cloud(), Empty(), Empty(), Cloud()],
               [Cloud(), Empty(), Empty(), Cloud()],
               [Cloud(), Empty(), Wings(), Cloud()],
               [Cloud(), Empty(), Empty(), Cloud()],
               [Cloud(), Empty(), Empty(), Cloud()],
               [Cloud(), Girl(), Empty(), Cloud()] ]
        expected = \
'''\
OY O
O  O
O  O
O wO
O  O
O  O
Ov O
O xO
O  O'''
        map_lines = view.get_map_lines(game_map)
        actual = '\n'.join(map_lines)
        self.assertEqual(expected, actual)

    def test_add_other_elements(self):
        scr = None
        view = TerminalView(scr)
        game_map = [[Cloud(), Empty(), Empty(), Cloud()],
                    [Cloud(), Girl(), Empty(), Cloud()]]
        map_lines = view.get_map_lines(game_map)
        decorated_map_lines = view.add_other_elements(map_lines)
        actual = '\n'.join(decorated_map_lines)
        
        expected = \
r'''OY O                                                                        _ .--.
O  O                                                                       ( `    )
                                                                        .-'      `--,
              w/a/s/d : movement                             _..----.. (             )`-.
              r/t : next/previous item                     .'_|` _|` _|(  .__,           )
              f : use selected item                       /_|  _|  _|  _(        (_,  .-'
              q : quit                                   ;|  _|  _|  _|  '-'__,--'`--'
                                                         | _|  _|  _|  _| |
                                                     _   ||  _|  _|  _|  _|
                                                   _( `--.\_|  _|  _|  _|/
                                                .-'       )--,|  _|  _|.`
                                               (__, (_      ) )_|  _| /
                                                 `-.__.\ _,--'\|__|__/
                                                               ;____;
                                                                \YT/
                                                                 ||
                                                                |""|
                                                                '==' '''[:-1]
        self.assertEqual(expected, actual)


class TestCell(unittest.TestCase):
    def test_hero_interaction(self):
        hero = Girl()
        self.assertEqual(Hero.default_movepoints, hero.movepoints)
        self.assertFalse(hero.inventory)

        cloud = Cloud()
        cell1, cell2 = hero.interact(cloud)
        self.assertTrue(isinstance(cell1, Girl))
        self.assertTrue(isinstance(cell2, Cloud))
        self.assertEqual(Hero.default_movepoints, hero.movepoints)

        thundercloud = ThunderCloud()
        cell1, cell2 = hero.interact(thundercloud)
        self.assertEqual(Hero.default_movepoints - ThunderCloud.default_power, hero.movepoints)
        self.assertTrue(isinstance(cell1, Girl))
        self.assertTrue(isinstance(cell2, ThunderCloud))

        bird = Bird()
        cell1, cell2 = hero.interact(bird)
        self.assertEqual(Hero.default_movepoints - ThunderCloud.default_power\
                                                 - Bird.default_power\
                                                 - 1, hero.movepoints)
        self.assertTrue(isinstance(cell1, Empty))
        self.assertTrue(isinstance(cell2, Girl))

        item = Wings()
        cell1, cell2 = hero.interact(item)
        self.assertEqual(1, len(hero.inventory))
        self.assertTrue(isinstance(hero.inventory[0], Wings))
        self.assertEqual(Hero.default_movepoints - ThunderCloud.default_power\
                                                 - Bird.default_power\
                                                 - 2, hero.movepoints)
        self.assertTrue(isinstance(cell1, Empty))
        self.assertTrue(isinstance(cell2, Girl))

    def test_bird_interaction(self):
        Bird.total_birds = 0
        bird = Bird()
        self.assertEqual(0, bird.id)
        
        cloud = Cloud()
        cell1, cell2 = bird.interact(cloud)
        self.assertTrue(isinstance(cell1, Bird))
        self.assertTrue(isinstance(cell2, Cloud))

        thundercloud = ThunderCloud()
        cell1, cell2 = bird.interact(thundercloud)
        self.assertTrue(isinstance(cell1, Bird))
        self.assertTrue(isinstance(cell2, ThunderCloud))        

        other_bird = Bird()
        self.assertEqual(1, other_bird.id)
        cell1, cell2 = bird.interact(other_bird)
        self.assertTrue(isinstance(cell1, Bird))
        self.assertTrue(isinstance(cell2, Bird))
        self.assertEqual(0, cell1.id)
        self.assertEqual(1, cell2.id)

        item = Wings()
        cell1, cell2 = bird.interact(item)
        self.assertTrue(isinstance(cell1, Bird))
        self.assertTrue(isinstance(cell2, Wings))


class ModelTest(unittest.TestCase):
    def test_map_1_start_position(self):
        expected = \
r'''O                O
O          v     O
O                O
O                O
O                O
O   v       v    O
O@@@@@  @@@@@@@@@O
O                O
O                O
O  x   w         O
O    Y           O'''
        view = TerminalView(None)
        controller = None
        model = Model(view, controller)
        map_lines = view.get_map_lines(model.map)
        self.assertEqual(expected, '\n'.join(map_lines))

    def test_map_1_take_wings_and_use(self):
        expected = \
r'''O                O
O  x   Y         O
O                O'''
        expected_2 = \
r'''O                O
O  xY            O
O                O'''
        view = TerminalView(None)
        view.draw = MagicMock()
        controller = None
        model = Model(view, controller)

        hero = model.get_cell(model.hero_position)
        model.execute("right")
        model.execute("up")
        model.execute("right")
        map_lines = view.get_map_lines(model.map)
        self.assertEqual(expected, '\n'.join(map_lines[-3:]))
        self.assertEqual(1, len(hero.inventory))
        self.assertTrue(isinstance(hero.inventory[0], Wings))
        model.execute("use item")
        self.assertEqual(0, len(hero.inventory))
        self.assertTrue(isinstance(hero.vehicle, Wings))
        model.execute("left")
        map_lines = view.get_map_lines(model.map)
        self.assertEqual(expected_2, '\n'.join(map_lines[-3:]))

if __name__ == '__main__':
    unittest.main()
