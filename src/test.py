import unittest

# from TerminalView import *
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

if __name__ == '__main__':
    # print "haha"
    unittest.main()
