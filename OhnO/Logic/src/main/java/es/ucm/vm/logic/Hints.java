package es.ucm.vm.logic;

//import java.awt.image.DirectColorModel;

import static es.ucm.vm.logic.BoardPosition.DIRECTIONS;

/**
 * Class that contains all the hint methods that, applied to a board, allow us to solve it.
 */
public class Hints {
    public Watcher _watcher;
    Board _board;
    BoardPosition _auxPos;
    public boolean _sameMap = false;

    /**
     * Constructor. Takes a board and creates a Watcher utility for its use inside hint methods
     * @param b (Board) board we want to apply hints to
     */
    public Hints(Board b)
    {
            _board = new Board(b.getMapSize());
            updateMap(b);
            _watcher = new Watcher(_board);
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
            // todo handle exception
        }
    }
    public Board getBoard(){
        return _board;
    }
    public void renderPrueba(){
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
     * Uses the Hint methods to solve a map and modify it to be solved.
     * @return (boolean) false if the map could not be solved, true if the given map is solvable
     */
    public boolean solveMap()
    {
        _sameMap = true;
        for(BoardTile[] column : _board.getMap())
        {
            for(BoardTile t : column)
            {
                switch (t._tileColor)
                {
                    case BLUE:
                        tryHintsOnBlue(t);
                        if(t._count > 0 && (checkTooMuchBlue(t) || checkTooMuchRed(t))) return false;
                        break;
                    case GREY:
                        tryHintsOnGrey(t);
                        break;
                }
            }
        }
        if(!_sameMap && !isValid()){
            return solveMap();
        }

        return isValid();
    }
    private void setTileInfo(BoardTile t, Board mapToSolve){
        TileInfo info = t._tileInfo;
        int possibleDirCount = 0;
        BoardPosition lastPossibleDirection = null;
        if(t._tileInfo == null) {
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

        }
        else {
            int d = 0;
            for (BoardPosition dir : DIRECTIONS) {
                TileInfo.TileInfoInDir infoDir = info.directionInfo[d];

                if (mapToSolve.getTileInDir(t, dir) != null)
                {
                    for (BoardTile nextTile = mapToSolve.getTileInDir(t, dir); nextTile._tileColor != TileColor.RED; nextTile = mapToSolve.getTileInDir(nextTile, dir)) {

                        if ((nextTile._tileColor == TileColor.BLUE && nextTile._count > 0) && info.numberReached) {
                            info.completedNumbersAround = true; // a single happy number was found around
                        }
                        if (mapToSolve.getTileInDir(nextTile, dir) == null)
                            break;
                    }
                }
                // if we originate FROM a number, and there are unknowns in this direction
                if ((t._tileColor == TileColor.BLUE && t._count > 0) && !info.numberReached && infoDir.unknownCount > 0) {
                    // check all directions other than this one
                    infoDir.maxPossibleCountInOtherDirections = 0;
                    int i  = 0;
                    for (BoardPosition otherDir : DIRECTIONS) {
                        if (otherDir != dir)
                            infoDir.maxPossibleCountInOtherDirections += info.directionInfo[i].maxPossibleCount;
                        i++;
                    }
                }
                d++;
            }
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
    public boolean newSolveMap(Board map, boolean trySolve){
        Board mapToSolve;
        //if they don't give us a map, we can use hint's map
        mapToSolve = (map == null) ? _board : map;
        boolean tryAgain = true;
        int attempts = 0;
        BoardTile hintTile = null;
        BoardTile[] pool = new BoardTile[mapToSolve.getMapSize()*mapToSolve.getMapSize()];

        while (tryAgain && attempts++ < 99) {

            tryAgain = false;

            if(trySolve) if(isValid()) return true;
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
            for (int i = 0; i < pool.length; i++) {
                BoardTile tile = pool[i];
                setTileInfo(tile, mapToSolve);
                TileInfo info = tile._tileInfo;
                // dots with no empty tiles in its paths can be fixed
                if ((tile._tileColor == TileColor.BLUE && tile._count == 0) && info.unknownsAround == 0 &&  info.numberCount > 0) {
                    tile.updateCount(info.numberCount);
                    tryAgain = true;//HintType.NumberCanBeEntered;
                    break;
                }
                //if blue but dont see any blue or gray. you should be red... sorry
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
                        /*if (hintMode)
                            hintTile = tile;
                        else*/
                        closeWithRed(tile, mapToSolve);
                        tryAgain = true; //HintType.ValueReached;
                        break;
                    }

                    // if a tile has only one direction to go, fill the first unknown there with a dot and retry
                    if (info.singlePossibleDirection != null) {
                        /*if (hintMode)
                            hintTile = tile;
                        else*/
                        BoardTile nextTile = mapToSolve.getTileInDir(tile, info.singlePossibleDirection);
                        if(nextTile._tileColor == TileColor.GREY) nextTile.updateTileColor(TileColor.BLUE);
                        //tile.closeDirection(info.singlePossibleDirection, true, 1);
                        tryAgain = true;//HintType.OneDirectionLeft;
                        break;
                    }
                    // if its number CAN be reached by filling out exactly the remaining unknowns, then do so!
                    //else if (info.canBeCompletedWithUnknowns) {
                    //console.log(tile.x, tile.y)
                    //tile.close(true);
                    //tryAgain = true;
                    //}

                    // check if a certain direction would be too much
                    int temporal = 0;
                    for (BoardPosition dir : DIRECTIONS) {
                        TileInfo.TileInfoInDir curDir = info.directionInfo[temporal];
                        if (curDir.wouldBeTooMuch) {
                            /*if (hintMode)
                                hintTile = tile;
                            else*/
                            BoardTile nextTile = mapToSolve.getTileInDir(tile, dir);
                            if(nextTile._tileColor == TileColor.GREY) nextTile.updateTileColor(TileColor.RED);
                            tryAgain = true; //HintType.WouldExceed;
                            break;
                        }
                        // if dotting one unknown tile in this direction is at least required no matter what
                        else if (curDir.unknownCount > 0 && curDir.numberWhenDottingFirstUnknown + curDir.maxPossibleCountInOtherDirections <= tile._count) {
                            /*if (hintMode)
                                hintTile = tile;
                            else
                                */
                            BoardTile nextTile = mapToSolve.getTileInDir(tile, dir);
                            if(nextTile._tileColor == TileColor.GREY) nextTile.updateTileColor(TileColor.BLUE);
                            tryAgain = true;//HintType.OneDirectionRequired;
                            break;
                        }
                        temporal++;
                    }
                    // break out the outer for loop too
                    if (tryAgain)
                        break;
                }
                // if a number has its required value around, but still an empty tile somewhere, close it
                // (this core regards that situation FROM the empty unknown tile, not from the number itself)
                // (but only if there are no tiles around that have a number and already reached it)
                if ((tile._tileColor == TileColor.GREY) && info.unknownsAround == 0 && !info.completedNumbersAround) {
                    if (info.numberCount == 0) {
                        /*if (hintMode)
                            hintTile = tile;
                        else*/
                        tile.updateTileColor(TileColor.RED);
                        tryAgain = true;//HintType.MustBeWall;
                        break;
                    }
                    //else if (info.numberCount > 0) {
                    //tile.number(info.numberCount);
                    //tryAgain = 7;
                    //break;
                    //}
                }
            }
        }
        return false;
    }

    public boolean colorTileIsValid(BoardTile t){

        if(t._boardPos._x == 0 && t._boardPos._y == 0) return true;
        //si es gris da igual
        if(t._tileColor == TileColor.GREY)
            return true;
        //is blue or gray now a valid color?
        if(t._tileColor == TileColor.BLUE)
            if (checkTooMuchBlue(t) || checkTooMuchRed(t)) return false;

        if(t._tileColor != TileColor.RED)
            if(checkIfRed(t)) return false;
        //see if the last constructed map will be destroyed by this tile
        int init_X = Math.max(t._boardPos._x- _board.getMapSize(), 0);
        int init_Y = Math.max(t._boardPos._y- _board.getMapSize(), 0);
        for(int y = init_Y; y <= t._boardPos._y; y++){
            for(int x = init_X; x < _board.getMapSize() && y <= t._boardPos._y; x++)
            {
                BoardTile actual_Tile = _board.getMap()[x][y];
                if(t._tileColor == TileColor.BLUE && actual_Tile._tileColor == TileColor.BLUE)
                    if(checkTooMuchBlue(actual_Tile)) return false;
                if(actual_Tile._tileColor == TileColor.BLUE)
                    if(checkTooMuchRed(actual_Tile)) return false;
            }
        }

        return true;
    }

    /**
     * Uses the hint methods to display a string with a hint depending on what can be done
     * @return (String) string with the helper hint
     */
    public String helpUser(){
        for(BoardTile[] column : _board.getMap())
        {
            for(BoardTile t : column)
            {
                switch (t._tileColor) {
                    case BLUE:
                        if(t._count > 0) {
                            if (checkTooMuchBlue(t))
                                return t._boardPos._x + "x" + t._boardPos._y + " is seeing to much";
                            if (checkTooMuchRed(t))
                                return t._boardPos._x + "x" + t._boardPos._y + " is closed but needs more blue tiles";
                            if (checkIfRed(t))
                                return t._boardPos._x + "x" + t._boardPos._y + " cannot be blue";
                            if(checkVisibleFulfilled(t) && !isClosed(t))
                                return t._boardPos._x + "x" + t._boardPos._y + " can be closed with red tiles";
                            for (BoardPosition dir: DIRECTIONS)
                            {
                                if (checkNoMoreBlue(t, dir) && _board.getMap()[t._boardPos._x + dir._x][t._boardPos._y + dir._y]._tileColor != TileColor.RED) {
                                    return t._boardPos._x + "x" + t._boardPos._y +
                                                            " needs to be closed at x:" + dir._x + " y:" + dir._y;
                                }
                                if (checkForcedBlue(t, dir) && _board.getMap()[t._boardPos._x + dir._x][t._boardPos._y + dir._y]._tileColor != TileColor.RED && !isClosed(t)) {
                                    return t._boardPos._x + "x" + t._boardPos._y +
                                            " needs blue tiles at x:" + dir._x + " y:" + dir._y;
                                }
                            }
                        }
                        break;
                    case GREY:
                            if (checkIfRed(t))
                                return t._boardPos._x + "x" + t._boardPos._y + " needs to be red";
                    break;
                }
            }
        }

        return "Think more about the board";
    }

    /**
     * Adds the "views"/counter info to blue tiles that currently don't have it
     */
    public void reCountEmpty()
    {
        for(BoardTile[] column : _board.getMap())
        {
            for(BoardTile t : column) {
                if(t._tileColor == TileColor.BLUE && t._count == 0) {
                    for (BoardPosition dir: DIRECTIONS)
                    {
                        t._count += _watcher.tilesInfFrontOf(t._boardPos, dir, TileColor.BLUE);
                    }
                }
            }
        }
    }

    /**
     * Checks for puzzle completion by looking for grey tiles. If no grey tiles remain, it's a win
     * @return (boolean) true if no grey tiles, false if one or more grey tiles remaining
     */
    public boolean isSolved()
    {
        for(BoardTile[] column : _board.getMap())
        {
            for(BoardTile t : column) {
                if(t._tileColor == TileColor.GREY) return false;
                if(t._tileColor == TileColor.BLUE)
                    if(t._count > 0) { if(checkTooMuchBlue(t)) return false; }
                    else if(checkIfRed(t)) return false;
            }
        }
        return true;
    }

    private boolean isValid(){
        for(BoardTile[] column : _board.getMap())
        {
            for(BoardTile t : column) {
                if(t._tileColor == TileColor.BLUE && t._count > 0)
                    if(!checkVisibleFulfilled(t)) return false;
            }
        }
        return true;
    }

    /**
     * Checks a tile for grey-specific hints
     * @param t (BoardTile) tile we want to check
     */
    void tryHintsOnGrey(BoardTile t)
    {
        // HINT 5   --  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --
        if(checkIfRed(t)) {
            t._tileColor = TileColor.RED;
            _sameMap = false;
            //renderPrueba();
        }
    }

    /**
     * Runs hints that involve a blue tile
     * @param t (CounterTile) blue tile we want to check
     */
    void tryHintsOnBlue(BoardTile t)
    {
        if (t._count == 0) return;

        // HINT 1   --  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --
        if(checkVisibleFulfilled(t))
        {
            closeWithRed(t, null);
            //renderPrueba();
        }
        else {
            // HINT 2   --  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --
            for (BoardPosition dir: DIRECTIONS)
            {
                if (checkNoMoreBlue(t, dir) && _board.getMap()[t._boardPos._x + dir._x][t._boardPos._y + dir._y]._tileColor != TileColor.RED) {
                    _board.getMap()[t._boardPos._x + dir._x][t._boardPos._y + dir._y]._tileColor = TileColor.RED;
                    _sameMap = false;
                    //renderPrueba();
                }
            }

            // HINT 3 (BUGGY)   --  --  --  --  --  --  --  --  --  --  --  --  --  --
            for (BoardPosition dir: DIRECTIONS)
            {
                if (checkForcedBlue(t, dir)) {
                    int blueCount = _watcher.tilesInfFrontOf(t._boardPos, dir, TileColor.BLUE);
                    _auxPos._x = (dir._x * blueCount) + dir._x + t._boardPos._x;
                    _auxPos._y = (dir._y * blueCount) + dir._y + t._boardPos._y;
                    if(!_board.offLimits(_auxPos))
                        if(_board.getMap()[_auxPos._x][_auxPos._y]._tileColor == TileColor.GREY){
                            _board.getMap()[_auxPos._x][_auxPos._y]._tileColor = TileColor.BLUE;
                            _sameMap = false;
                        }
                    //renderPrueba();
                }
            }
        }
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
                blueCount = _watcher.tilesInfFrontOf(t._boardPos, dir, TileColor.BLUE);
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
     * Counts all visible blue tiles surrounding the given tile
     * @param t (CounterTile) blue tile we want to count for
     * @return (int) number of visible blue tiles
     */
    int countVisibleBlue(BoardTile t)
    {
        int counted = 0;
        for (BoardPosition dir: DIRECTIONS)
        {
            counted += _watcher.tilesInfFrontOf(t._boardPos, dir, TileColor.BLUE);
        }

        return counted;
    }

    /**
     * Aux function that checks if a tile is closed off
     * @param t (BoardTile) tile we want to check
     * @return (boolean) true if it is closed, false if not
     */
    boolean isClosed(BoardTile t){
        int lego , legos;
        for(BoardPosition dir: DIRECTIONS)
        {
            if(!_board.offLimits(BoardPosition.add(t._boardPos, dir))) {
                lego = _watcher.closerTile(t._boardPos, dir, TileColor.RED);
                legos = _watcher.closerTile(t._boardPos, dir, TileColor.GREY);
                if (lego == -1 && legos != -1) return false;
            }
        }
        return true;
    }

    // ---------------------------------------------------------------------------------------------
    //                                H I N T    M E T H O D S
    // ---------------------------------------------------------------------------------------------

    /**
     * Checks if a blue tile already sees all the necessary blue tiles. Then it can be closed off
     * with red tiles
     * @param c (CounterTile) blue tile to be checked
     * @return (boolean) true if the seen tiles are the same as the blue tiles it neeeds to see
     */
    public boolean checkVisibleFulfilled(BoardTile c) {
        int counted = countVisibleBlue(c);

        return c._count == counted;
    }


    /**
     * Checks if a tile doesn't allow more blue visible tiles in a given direction
     * @param c (CounterTile) blue tile to be checked
     * @param dir (BoardPosition) the direction that will be looking for
     * @return (boolean) true if the tile doesn't need more blue
     */
    public boolean checkNoMoreBlue(BoardTile c, BoardPosition dir) {
        int counted;
        BoardPosition _newPos = BoardPosition.add(c._boardPos, dir);

        if (_board.offLimits(_newPos)) return false;

        TileColor last_c = _board.getMap()[_newPos._x][_newPos._y]._tileColor;
        if(last_c == TileColor.BLUE) return false;
        _board.getMap()[_newPos._x][_newPos._y]._tileColor = TileColor.BLUE;

        counted = countVisibleBlue(c);

        _board.getMap()[_newPos._x][_newPos._y]._tileColor = last_c;

        return  c._count < counted;
    }

    /**
     * Hint that checks if there is mandatory to put a blue tile, or else the count number wouldn't
     * be able to be achieved
     * @param c (BoardTile) tile we want to check
     * @param dir (BoardPosition) direction we're checking in
     * @return (boolean) true if it needs a forced blue
     */
    public boolean checkForcedBlue(BoardTile c, BoardPosition dir) {
        int _free = 0;
        BoardPosition _newPos = BoardPosition.add(c._boardPos, dir);
        if (_board.offLimits(_newPos)) return false;

        int leg;
        if(!BoardPosition.compare(dir, new BoardPosition(0, 1))){
            leg = _watcher.closerTile(c._boardPos, new BoardPosition(0, 1), TileColor.RED);
            _free += (leg == -1) ? _board.getMapSize() - BoardPosition.add(c._boardPos, new BoardPosition(0, 1))._y : leg;

        }
        if(!BoardPosition.compare(dir, new BoardPosition(1, 0))){
            leg = _watcher.closerTile(c._boardPos, new BoardPosition(1, 0), TileColor.RED);
            _free += (leg == -1) ? _board.getMapSize() - BoardPosition.add(c._boardPos, new BoardPosition(1, 0))._x : leg;
        }
        if(!BoardPosition.compare(dir, new BoardPosition(0, -1))) {
            leg = _watcher.closerTile(c._boardPos, new BoardPosition(0, -1), TileColor.RED);
            _free += (leg == -1) ? c._boardPos._y : leg;
        }
        if(!BoardPosition.compare(dir, new BoardPosition(-1, 0))){
            leg = _watcher.closerTile(c._boardPos, new BoardPosition(-1, 0), TileColor.RED);
            _free += (leg == -1) ? c._boardPos._x : leg;
        }

        return _free < c._count;
    }

    /**
     * Checks if a tile has too much blue surrounding it
     * @param c (BoardTile) tile we want to check
     * @return (boolean) true if there is too much blue
     */
    public boolean checkTooMuchBlue(BoardTile c) {
        int counted = countVisibleBlue(c);

        return c._count < counted;
    }

    /**
     * Checks if a tile has too much red surrounding it: it doesn't see enough blue tiles
     * @param c (BoardTile) tile we want to check
     * @return (boolean) true if there is too much red
     */
    public boolean checkTooMuchRed(BoardTile c) {
        int free = 0, lego;

        lego = _watcher.closerTile(c._boardPos, new BoardPosition(0, 1), TileColor.RED);
        free += (lego == -1) ? _board.getMapSize() - BoardPosition.add(c._boardPos, new BoardPosition(0, 1))._y : lego;
        lego = _watcher.closerTile(c._boardPos, new BoardPosition(1, 0), TileColor.RED);
        free += (lego == -1) ? _board.getMapSize() - BoardPosition.add(c._boardPos, new BoardPosition(1, 0))._x : lego;
        lego = _watcher.closerTile(c._boardPos, new BoardPosition(0, -1), TileColor.RED);
        free += (lego == -1) ? c._boardPos._y : lego;
        lego = _watcher.closerTile(c._boardPos, new BoardPosition(-1, 0), TileColor.RED);
        free += (lego == -1) ? c._boardPos._x : lego;

        return c._count > free;
    }

    /**
     * Checks if a tile should be red (if a tile doesn't see any blue tiles, it has to be red)
     * @param c (BoardTile) tile we want to check
     * @return (boolean) true if the tile has to be red
     */
    public boolean checkIfRed(BoardTile c) {
        int free = 0, lego;

        lego = _watcher.closerTile(c._boardPos, new BoardPosition(0, 1), TileColor.RED);
        free += (lego == -1) ? _board.getMapSize() - BoardPosition.add(c._boardPos, new BoardPosition(0, 1))._y : lego;
        lego = _watcher.closerTile(c._boardPos, new BoardPosition(1, 0), TileColor.RED);
        free += (lego == -1) ? _board.getMapSize() - BoardPosition.add(c._boardPos, new BoardPosition(1, 0))._x : lego;
        lego = _watcher.closerTile(c._boardPos, new BoardPosition(0, -1), TileColor.RED);
        free += (lego == -1) ? c._boardPos._y : lego;
        lego = _watcher.closerTile(c._boardPos, new BoardPosition(-1, 0), TileColor.RED);
        free += (lego == -1) ? c._boardPos._x : lego;

        return free <= 0;
    }

    public boolean checkWrongBlue() {
        return false;
    }
}
