package es.ucm.vm.logic;

//import java.awt.image.DirectColorModel;

import java.util.Collections;
import java.util.Random;
import java.util.Stack;

import static es.ucm.vm.logic.BoardPosition.DIRECTIONS;

/**
 * Class that contains all the hint methods that, applied to a board, allow us to solve it.
 */
public class Hints {
    Board _board;
    BoardPosition _auxPos;
    public boolean _sameMap = false;
    public BoardTile _hintedTile = null;

    /**
     * Constructor. Takes a board for future hint calculations
     * @param b (Board) board we want to apply hints to
     */
    public Hints(Board b)
    {
        _board = new Board(b.getMapSize());
        updateMap(b);
        _auxPos = new BoardPosition(0, 0);
    }

    /**
     * Performs a deep copy of the board to update the internal _board
     * @param b (Board) new board we want to copy
     */
    public void updateMap(Board b)
    {
        try {
            int x = 0, y = 0;
            for (BoardTile[] column : b.getMap()) {
                for (BoardTile t : column) {
                    _board.getMap()[x][y] = (BoardTile) t.clone();
                    y++;
                }
                y = 0;
                x++;
            }
        } catch (Exception e){
            System.err.println(e.getMessage());
        }
    }

    /**
     * Debugging method that uses system.out to write board configuration
     */
    public void debugRender(){
        for (int y = 0; y <  _board.getMapSize(); y++) {
            System.out.println("+---+---+---+---+---+---+---+---+---+");
            for (int x = 0; x <  _board.getMapSize(); x++) {
                if (_board.getMap()[x][y]._tileColor == TileColor.BLUE)
                    System.out.print("| " + _board.getMap()[x][y]._count + " ");
                else if (_board.getMap()[x][y]._tileColor == TileColor.RED)
                    System.out.print("| X ");
                else System.out.print("|   ");
            }
            System.out.println("|");
        }
        System.out.println("+---+---+---+---+---+---+---+---+---+");
    }


    /**
     * Uses hint mechanics to store tile info such as number of blues it sees, greys around it, etc.
     * This info will be used later for hints and map solving
     *
     * @param t (BoardTile) tile we want to check
     * @param mapToSolve (Board) map, for checking out adjacent tiles
     */
    private void setTileInfo(BoardTile t, Board mapToSolve){
        TileInfo info;
        int possibleDirCount = 0;
        BoardPosition lastPossibleDirection = null;
            t._tileInfo = new TileInfo();
            t._tileInfo.init();
            info = t._tileInfo;
            int d = 0;
            //We check every direction
            for (BoardPosition dir : DIRECTIONS) {
                TileInfo.TileInfoInDir infoDir = t._tileInfo.directionInfo[d];

                if (mapToSolve.getTileInDir(t, dir) != null){
                    //The direction's check will stop when see a wall or we be out the map boundaries
                    for (BoardTile nextTile = mapToSolve.getTileInDir(t, dir); nextTile._tileColor != TileColor.RED; nextTile = mapToSolve.getTileInDir(nextTile, dir)) {
                        if (nextTile._tileColor == TileColor.GREY) {
                            if (infoDir.unknownCount == 0)
                                infoDir.numberWhenDottingFirstUnknown++;

                            infoDir.unknownCount++;
                            infoDir.maxPossibleCount++;
                            info.unknownsAround++;

                            if ((t._tileColor == TileColor.BLUE && t._count > 0) && lastPossibleDirection != dir) {
                                possibleDirCount++;
                                lastPossibleDirection = dir;
                            }
                        } else if (nextTile._tileColor == TileColor.BLUE) {
                            infoDir.maxPossibleCount++;
                            // if no unknown found yet in this direction
                            if (infoDir.unknownCount == 0) {
                                info.numberCount++;
                                infoDir.numberWhenDottingFirstUnknown++;
                                if(nextTile._count > 0) {
                                    info.numberTileCount++;
                                }
                            }
                            // else if we were looking FROM a number, and we found a number with only 1 unknown in between...
                            else if ((t._tileColor == TileColor.BLUE && t._count > 0) && infoDir.unknownCount == 1) {
                                infoDir.numberCountAfterUnknown++;
                                infoDir.numberWhenDottingFirstUnknown++;
                                if (infoDir.numberCountAfterUnknown + 1 > t._count) {
                                    infoDir.wouldBeTooMuch = true;
                                }
                            }
                        }
                        if (mapToSolve.getTileInDir(nextTile, dir) == null)
                            break;
                    }
                }
                d++;
            }
            // if there's only one possible direction that has room to expand, set it
            if (possibleDirCount == 1) {
                info.singlePossibleDirection = lastPossibleDirection;
            }

        // see if this number's value has been reached, so its paths can be closed
        if ((t._tileColor == TileColor.BLUE && t._count > 0) && t._count == info.numberCount)
            info.numberReached = true;
        else if ((t._tileColor == TileColor.BLUE && t._count > 0)  && t._count == info.numberCount + info.unknownsAround)
            info.canBeCompletedWithUnknowns = true;

    }


    /**
     * Uses the tile info and hints to try to solve a map
     *
     * @param map (Board) external map, in case we want to solve it. If the map is null, it will
     *            use the stored one (_board)
     * @return (Boolean) true if the map was solved
     */
    public boolean solveMap(Board map){
        Board mapToSolve;

        //if they don't give us a map, we can use hint's map
        mapToSolve = (map == null) ? _board : map;

        boolean tryAgain = true;
        int attempts = 0;
        BoardTile[] pool = new BoardTile[mapToSolve.getMapSize()*mapToSolve.getMapSize()];

        while (tryAgain && attempts++ < 99) {

            tryAgain = false;

            if(map == null) {
                if (noGreysInMap(mapToSolve)) return true;
            }
            //we fill the pool
            int z = 0;
            for(BoardTile[] column : mapToSolve.getMap())
            {
                for(BoardTile t : column) {
                    pool[z] = t;
                    z++;
                }
            }
            //next pass collection, now we have full info
            for (BoardTile tile : pool) {
                setTileInfo(tile, mapToSolve);
                TileInfo info = tile._tileInfo;
                // dots with no empty tiles in its paths can be fixed
                if ((tile._tileColor == TileColor.BLUE && tile._count == 0) && info.unknownsAround == 0 && info.numberCount > 0) {
                    tile.updateCount(info.numberCount);
                    tryAgain = true;//HintType.NumberCanBeEntered;
                    break;
                }
                //if a blue tile doesnt see any other blue or gray, this tile is red
                if (tile._tileColor == TileColor.BLUE && tile._count == 0) {
                    tile.updateTileColor(TileColor.RED);
                    tile.updateCount(-1);
                    tryAgain = true;
                    break;
                }
                // if a number has unknowns around, perhaps we can fill those unknowns
                if ((tile._tileColor == TileColor.BLUE && tile._count >= 0) && info.unknownsAround > 0) {

                    // if its number is reached, close its paths by walls
                    if (info.numberReached) {
                        closeWithRed(tile, mapToSolve);
                        tryAgain = true; //HintType.ValueReached;
                        break;
                    }

                    // if a tile has only one direction to go, fill the first unknown there with a dot and retry
                    if (info.singlePossibleDirection != null) {
                        BoardTile nextTile = mapToSolve.getTileInDir(tile, info.singlePossibleDirection);
                        if (nextTile._tileColor == TileColor.GREY) {
                            nextTile.updateTileColor(TileColor.BLUE);
                        }
                        //tile.closeDirection(info.singlePossibleDirection, true, 1);
                        tryAgain = true;//HintType.OneDirectionLeft;
                        break;
                    }

                    // check if a certain direction would be too much
                    int temporal = 0;
                    for (BoardPosition dir : DIRECTIONS) {
                        TileInfo.TileInfoInDir curDir = info.directionInfo[temporal];
                        if (curDir.wouldBeTooMuch) {
                            BoardTile nextTile = mapToSolve.getTileInDir(tile, dir);
                            if (nextTile._tileColor == TileColor.GREY) {
                                nextTile.updateTileColor(TileColor.RED);
                            }
                            tryAgain = true; //HintType.WouldExceed;
                            break;
                        }
                        // if dotting one unknown tile in this direction is at least required no matter what
                        else if (curDir.unknownCount > 0 && curDir.numberWhenDottingFirstUnknown + curDir.maxPossibleCountInOtherDirections <= tile._count) {
                            BoardTile nextTile = mapToSolve.getTileInDir(tile, dir);
                            if (nextTile._tileColor == TileColor.GREY) {
                                nextTile.updateTileColor(TileColor.BLUE);
                            }
                            tryAgain = true;//HintType.OneDirectionRequired;
                            break;
                        }
                        temporal++;
                    }
                    // break out the outer for loop too
                    if (tryAgain)
                        break;
                }
            }
        }
        return false;
    }


    /**
     * Uses the hint methods to display a string with a hint depending on what can be done
     * @return (String) string with the helper hint
     */
    public String helpUser(){
        Stack<BoardTile> pool = new Stack<>();
        //we fill the pool
        for(BoardTile[] column : _board.getMap())
        {
            for(BoardTile t : column) {
                pool.push(t);
            }
        }
        Collections.shuffle(pool, new Random());
        while(!pool.isEmpty()) {
            BoardTile tile = pool.pop();
            setTileInfo(tile, _board);
            TileInfo info = tile._tileInfo;

            _hintedTile = tile;
            int posX = (tile._boardPos._x + 1), posY = (tile._boardPos._y + 1);

            if (tile._tileColor == TileColor.BLUE && (info.numberCount + info.unknownsAround) == 0) {
                return posX + "x" + posY + " cannot be blue";
            }
            if (tile._tileColor == TileColor.BLUE && tile._count > info.numberCount && info.unknownsAround == 0 && tile._count > 0) {
                return posX + "x" + posY + " is closed but needs more blue tiles";
            }
            if (tile._tileColor == TileColor.BLUE && tile._count < info.numberCount && tile._count > 0) {
                return posX + "x" + posY + " is seeing to much";
            }

            // if a number has unknowns around, perhaps we can fill those unknowns
            if ((tile._tileColor == TileColor.BLUE && tile._count > 0) && info.unknownsAround > 0) {
                // if its number is reached, close its paths by walls
                if (info.numberReached) {
                    return posX + "x" + posY + " can be closed with red tiles";
                }

                // if a tile has only one direction to go, fill the first unknown there with a dot and retry
                if (info.singlePossibleDirection != null) {
                    return posX + "x" + posY +
                            " needs blue tiles (" + info.singlePossibleDirection.dirToString() + ")";
                }

                // check if a certain direction would be too much
                int temporal = 0;
                for (BoardPosition dir : DIRECTIONS) {
                    TileInfo.TileInfoInDir curDir = info.directionInfo[temporal];
                    if (curDir.wouldBeTooMuch) {
                        return posX + "x" + posY +
                                " needs to be closed (" + dir.dirToString() + ")";
                    }
                    // if dotting one unknown tile in this direction is at least required no matter what
                    else if (curDir.unknownCount > 0 && curDir.numberWhenDottingFirstUnknown + curDir.maxPossibleCountInOtherDirections <= tile._count) {
                        return posX + "x" + posY + " needs more blue tiles";
                    }
                    temporal++;
                }
            }
        }

        return "Think more about the board";
    }

    /**
     * Checks for puzzle completion by looking for grey tiles. If no grey tiles remain, it's a win
     * @return (boolean) true if no grey tiles, false if one or more grey tiles remaining
     */
    public boolean isSolved() {
        for(BoardTile[] column : _board.getMap())
        {
            for(BoardTile t : column) {
                if(t._tileColor == TileColor.GREY) return false;
                if(t._tileColor == TileColor.BLUE){
                    setTileInfo(t, _board);
                    TileInfo info = t._tileInfo;
                    if(info.numberCount == 0) return false;
                    if(t._count < info.numberCount && t._count > 0) return false;
                    if(info.numberTileCount == 0 && t._count == 0) return false;
                }
            }
        }
        return true;
    }

    /**
     * Given a map, it checks if there are any grey tiles
     *
     * @param map (Board) board we want to check
     * @return (boolean) true if there are no grey tiles
     */
    public boolean noGreysInMap(Board map){
        for(BoardTile[] column : map.getMap())
        {
            for(BoardTile t : column) {
                if(t._tileColor == TileColor.GREY){
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * Given a tile, it checks around it and adds red tiles on the surrounding empty spaces.
     * @param t (CounterTile) tile we want to close. it has passed a checkVisibleFulfilled test
     */
    void closeWithRed(BoardTile t, Board map)
    {
        if(map == null) map = _board;
        if (!map.offLimits(t._boardPos))
        {
            int blueCount;
            for (BoardPosition dir: DIRECTIONS)
            {
                blueCount = bluesInfFrontOf(t._boardPos, dir);
                _auxPos._x = (dir._x * blueCount) + dir._x + t._boardPos._x;
                _auxPos._y = (dir._y * blueCount) + dir._y + t._boardPos._y;
                if(!map.offLimits(_auxPos) && map.getMap()[_auxPos._x][_auxPos._y]._tileColor == TileColor.GREY)
                {
                    map.getMap()[_auxPos._x][_auxPos._y].updateTileColor(TileColor.RED);
                    _sameMap = false;
                }
            }
        }
    }

    /**
     * Used to count the number of blue tiles in front of a given tile, and in a certain direction
     *
     * @param pos (BoardPos) position of the tile we want to check, in board coords
     * @param dir (BoardPos) direction of the check
     * @return (int) number of blue tiles in that direction
     */
    private int bluesInfFrontOf(BoardPosition pos, BoardPosition dir)
    {
        int counted = 0;
        BoardPosition _newPos = BoardPosition.add(pos, dir);

        while(!_board.offLimits(_newPos) && _board.getMap()[_newPos._x][_newPos._y]._tileColor == TileColor.BLUE)
        {
            counted++;
            _newPos = BoardPosition.add(_newPos, dir);
        }
        return counted;
    }
}
