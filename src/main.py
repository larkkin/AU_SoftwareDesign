from model import *

def main():
    FORMAT = '%(message)s'
    logging.basicConfig(filename='log.txt',
                        level=logging.INFO,
                        format=FORMAT,
                        filemode='w')
    stdscr = curses.initscr()
    view = TerminalView(stdscr)
    controller = TerminalController(stdscr)
    game_model = Model(view, controller)
    game_model.run()


if __name__ == '__main__':
    main()