import {ref} from "vue";
import router from "@/router";
import axios from "axios";
import {ElMessage} from "element-plus";

export const needLogin = async () => {
    const jwt = localStorage.getItem('jwt');
    if (jwt === undefined || jwt === null) {
        return true;
    }
    try {
        await axios({
            method: 'get',
            url: '/users/loggedin',
            headers: {
                authorization: `Bearer ${jwt}`
            }
        })
        return false;
    } catch (e) {
        return true;
    }
};

export const assertLogin = async () => {
    if (await needLogin()) {
        ElMessage("Not logined. Please login.");
        await router.push("/login");
    }
}

export const getJWTToken = () => {
    return localStorage.getItem('jwttoken');
}

export const getId = () => {
    return 1;
}

export const MessageSender = {
    User: 1,
    Opponent: 2
};

export const Interests = ref([
    {
        name: "Interest 0",
        id: 0
    },
    {
        name: "Interest 1",
        id: 1
    },
    {
        name: "Interest 2",
        id: 2
    },
    {
        name: "Interest 3",
        id: 3
    },
    {
        name: "Interest 4",
        id: 4
    },
    {
        name: "Interest 5",
        id: 5
    },
    {
        name: "Interest 6",
        id: 6
    },
    {
        name: "Interest 7",
        id: 7
    },
    {
        name: "Interest 8",
        id: 8
    },
    {
        name: "Interest 9",
        id: 9
    },
    {
        name: "Interest 10",
        id: 10
    },
    {
        name: "Interest 11",
        id: 11
    },
    {
        name: "Interest 12",
        id: 12
    },
    {
        name: "Interest 13",
        id: 13
    },
    {
        name: "Interest 14",
        id: 14
    },
    {
        name: "Interest 15",
        id: 15
    },
]);

export const Faculties = ref([
    {
        name: "Arts",
        value: 0
    },
    {
        name: "Engineering",
        value: 1
    },
    {
        name: "Environment",
        value: 2
    },
    {
        name: "Health",
        value: 3
    },
    {
        name: "Mathematics",
        value: 4
    },
    {
        name: "Science",
        value: 5
    }
]);

export const Genders = ref([
    {
        name: "Male",
        id: 0
    },
    {
        name: "Female",
        id: 1
    },
    {
        name: "Other",
        id: 2
    }
]);