import {ref} from "vue";

const reactive = {}

class ReactiveVariable {
    func
    element

    constructor(f) {
        this.func = f
        this.element = ref(f(window.innerWidth))
    }

    update() {
        this.element.value = this.func(window.innerWidth, window.innerHeight)
    }
}

function windowUpdated() {
    for (let [key, val] of Object.entries(reactive)) {
        val.update();
    }
}

export const registerReactiveVariable = (name, func) => {
    reactive[name] = new ReactiveVariable(func);
}

export const getReactiveVariable = (name) => {
    return reactive[name].element;
}

window.onresize = windowUpdated;