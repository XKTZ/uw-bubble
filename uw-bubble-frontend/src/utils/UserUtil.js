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
        name: "Sports",
        id: 0
    },
    {
        name: "Movie",
        id: 1
    },
    {
        name: "Reading",
        id: 2
    },
    {
        name: "Gaming",
        id: 3
    },
    {
        name: "Pets",
        id: 4
    },
    {
        name: "Technology",
        id: 5
    },
    {
        name: "Programming",
        id: 6
    },
    {
        name: "Mathematics",
        id: 7
    },
    {
        name: "Foods",
        id: 8
    },
    {
        name: "Art",
        id: 9
    },
    {
        name: "Literature",
        id: 10
    },
    {
        name: "Exercise",
        id: 11
    },
    {
        name: "Camping",
        id: 12
    },
    {
        name: "Photography",
        id: 13
    },
    {
        name: "Music",
        id: 14
    },
    {
        name: "Public Speaking",
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