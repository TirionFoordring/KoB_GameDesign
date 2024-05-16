const BaseObject = [];

export class Base_Object {
    constructor() {
        BaseObject.push(this);

        this.timedelta = 0;
        this.start_flag = false;
    }

    start() {

    }

    update() {

    }

    on_destroy() {

    }

    destroy() {
        this.on_destroy();
        for (let i in BaseObject) { //in遍历下标，of遍历数值
            const obj = BaseObject[i];
            if (obj === this) {
                BaseObject.splice(i); //splice表示删除数组中的某一个（或某一段）元素
                break;
            }
        }
    }
}

let last_timestamp; //记录上一时刻
const step = timestamp => {
    for (let obj of BaseObject) {
        if (!obj.start_flag) {
            obj.start_flag = true;
            obj.start();
        } else {
            obj.timedelta = timestamp - last_timestamp;
            obj.update();
        }
    }

    last_timestamp = timestamp;
    requestAnimationFrame(step);
}

requestAnimationFrame(step);