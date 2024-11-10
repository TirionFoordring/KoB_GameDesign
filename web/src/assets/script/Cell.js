export class cell {
    constructor(r, c) {
        this.r = r;
        this.c = c;
        //the center of the circle in a square is the top left corner + 0.5 
        this.x = c + 0.5;
        this.y = r + 0.5;
    }
}