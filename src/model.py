import numpy as np
from random import choice
from time import sleep
import sys
import os
from cell import *
from terminal_view import *
from controller import *

cell_types = {'girl', 'cloud', 'bird', 'thundercloud', 'wings', 'jetpack', 'nimbus2000', 'empty'}


'''Contains all the game rules and the game data'''
class Model:
    cell_from_string_dict = {'Y' : Girl,
                             'O' : Cloud,
                             '@' : ThunderCloud,
                             ' ' : Empty,
                             'v' : Bird,
                             'w' : Wings,
                             'x' : Quadrocopter}

    '''
    map is a rectangle -- np.matrix of game_objects (cells) 
    '''
    def __init__(self, view, controller, base_score=0, level_num=0):
        self.view = view
        self.controller = controller
        self.win = False
        self.lost = False
        self.crash = False
        self.end = False
        self.base_score = base_score
        self.score = base_score
        self.level_num = level_num

        self.levels = ["./maps/"+filename for filename in sorted(os.listdir("./maps"))]
        self.hero_position = None
        self.crashinfo = ''
        self.hero_inventory = []
        self.selected_item = 0
        self.metainfo = {'movepoints left' : Hero.default_movepoints,
                         'current vehicle' : None,
                         'score' : self.score}
        Bird.total_birds = 0
        self.read_map_from_file(self.levels[self.level_num])
        self.command_to_direction = {'left':  np.array((0, -1)),
                                     'right': np.array((0,  1)),
                                     'up':    np.array(( 1, 0)),
                                     'down':  np.array((-1, 0))}


    '''A method for a simple map generation. 
       Currently not in use, since pre-constructed maps are loaded from the file system'''             
    def generate_map(self, height, width):
        self.hero_position = np.array((0, 1))
        self.map = np.array([[Empty() for i in range(width)] for j in range(height)])
        for i in range(height):
            self.map[i, 0] = Cloud()
            self.map[i, width-1] = Cloud()
        self.map[self.hero_position[0], self.hero_position[1]] = Girl()
        self.bird_positions = [[13, 2], [13, 4], [11, 1], [6, 3]]
        for i in range(1, width - 1):
            if i == 3:
                continue
            self.map[height / 2, i] = ThunderCloud()
        for pos in self.bird_positions:
            self.map[pos[0], pos[1]] = Bird()
        self.map[3, 3] = Wings()
        self.map[2, 5] = Wings()
        self.map[4, 1] = Quadrocopter()

    '''The main method of the class. Waits for a command from the controller in an infinite loop'''
    def run(self):
        self.view.draw(self)
        stop = False
        while not stop:
            try:
                command = self.controller.get_command()
                stop = self.execute(command)
            except BaseException as e:
              self.crashinfo = str(e) + " " + str(e.args)
              self.crash = True
              self.view.draw(self)





    '''A generic method for executing a command, which can be one of the following:
       exit, change item selection, enable selected item, move character.
       Returns a boolean that tell the loop in "run" method whether to stop or not.'''
    def execute(self, command):
        if self.end:
            self.exit()
            return True
        if (self.win and self.level_num < (len(self.levels) - 1)):
            self.__init__(self.view, self.controller, self.score, self.level_num + 1)
            self.view.draw(self)
            return False
        if self.win:
            self.end = True
            self.view.draw(self)
            return False
        if self.lost or self.crash:
            command = 'exit'
        if command == 'exit':
            self.exit()
            return True
        elif command == 'illegal command':
            return False
        elif command == 'next item':
            if self.hero_inventory:
                self.selected_item = (self.selected_item + 1) % len(self.hero_inventory)
                self.view.draw(self)
            return False
        elif command == 'prev item':
            if self.hero_inventory:
                self.selected_item = (self.selected_item - 1) % len(self.hero_inventory)
            self.view.draw(self)
            return False
        elif command == 'use item':
            if self.hero_inventory:
                item = self.hero_inventory.pop(self.selected_item)
                hero = self.get_cell(self.hero_position)
                hero.enable_vehicle(item)
                self.metainfo['current vehicle'] = hero.vehicle
                self.execute('prev item')
            return False

        hero_direction = self.command_to_direction[command]
        self.move(self.hero_position, hero_direction)
        for bird_pos in self.bird_positions:
            if bird_pos[0] < 0:
                continue
            sleep(0.01)
            bird_direction = (choice(list(self.command_to_direction.values())))
            self.move(bird_pos, bird_direction)
        return False


    '''A method that handles moving the character (and, hence, all the NPCs)'''
    def move(self, cell_coordinates, direction):
        cell_1 = self.map[cell_coordinates[0], cell_coordinates[1]]
        if isinstance(cell_1, Hero):
            for i in range(1, cell_1.speed):
                cell_coordinates_2 = cell_coordinates + direction
                if cell_coordinates_2[0] >= len(self.map):
                    self.win = True
                    break
                elif cell_coordinates_2[0] < 0:
                    break
                elif isinstance(self.get_cell(cell_coordinates_2), Empty):
                    self.unsafe_move(cell_1, cell_coordinates, direction)
                    cell_coordinates += direction
                    cell_1 = self.get_cell(cell_coordinates)
                    self.add_movepoints(1)
                else:
                    break

        cell_coordinates_2 = cell_coordinates + direction
        if isinstance(cell_1, Hero) and cell_coordinates_2[0] >= len(self.map):
            self.win = True
        elif isinstance(cell_1, Hero) and cell_coordinates_2[0] < 0:
            pass
        elif isinstance(cell_1, Bird) and cell_coordinates_2[0] >= len(self.map) or cell_coordinates_2[0] < 0:
            pass
        else:
            self.unsafe_move(cell_1, cell_coordinates, direction)
        self.view.draw(self)

    '''A part of the "move" method. Can be called only if the movement is possible'''
    def unsafe_move(self, cell_1, cell_coordinates, direction):
        cell_coordinates_2 = cell_coordinates + direction
        cell_2 = self.get_cell(cell_coordinates_2)
        self.clear_position(cell_1)
        self.clear_position(cell_2)
        cell_1, cell_2 = cell_1.interact(cell_2)
        self.set_cell(cell_coordinates, cell_1)
        self.set_cell(cell_coordinates_2, cell_2)
        self.update_positions(cell_1, cell_coordinates)
        self.update_positions(cell_2, cell_coordinates_2)

    def add_movepoints(self, movepoints):
        hero = self.get_cell(self.hero_position)
        hero.movepoints += movepoints
        self.metainfo['movepoints left'] += 1

    def get_cell(self, cell_coordinates):
        return self.map[cell_coordinates[0], cell_coordinates[1]]
    def set_cell(self, cell_coordinates, cell):
        self.map[cell_coordinates[0], cell_coordinates[1]] = cell

    '''Removes a bird from the game field'''
    def clear_position(self, cell):
        if isinstance(cell, Bird):
            self.bird_positions[cell.id] = np.array((-1, -1))   
    '''Updates information that Model keeps about PC or NPC'''
    def update_positions(self, cell, coordinates):
        if isinstance(cell, Hero):
            self.metainfo['movepoints left'] = cell.movepoints
            self.metainfo['current vehicle'] = cell.vehicle
            self.hero_inventory = cell.inventory
            self.hero_position = coordinates
            self.score = self.base_score + max(self.score - self.base_score, self.hero_position[0])
            self.metainfo['score'] = self.score
            if self.metainfo['movepoints left'] <= 0:
                self.lost = True
        if isinstance(cell, Bird):
            self.bird_positions[cell.id] = coordinates
    
    '''A method for reading the game map from a file'''
    def read_map_from_file(self, filename):
        with open(filename) as inp:
            self.map = []
            self.bird_positions = []
            line = inp.readline()
            movepoints = int(line.strip().split(':')[-1])
            lines = [st.strip() for st in inp if st.strip()]
            for i, line in enumerate(lines[::-1]):
                row = []
                for symb in line:
                    cell = self.read_cell(symb)
                    if isinstance(cell, Hero):
                        self.hero_position = np.array((i, len(row)))
                        cell.movepoints = movepoints
                    elif isinstance(cell, Bird):
                        self.bird_positions.append(np.array((i, len(row))))
                    row.append(cell)
                self.map.append(row)
            self.map = np.array(self.map)
            width = len(self.map[0])
            height = len(self.map)
            for row in self.map:
                if len(row) != width:
                    raise Exception("unequal lengths of rows in map")
    def read_cell(self, symb):
        return Model.cell_from_string_dict[symb]()
    
    def exit(self):
        self.view.exit()
