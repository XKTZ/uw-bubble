<template>
  <div style="margin-top: .5%; margin-bottom: .5%;">
    <el-card>
      <div style="line-height: 30px; text-align: left;">
        <span class="component">
          <el-avatar :size="45" :src="header"/>
        </span>
        <span class="component" style="font-weight: 600;">
          {{ name }}
        </span>
        <span class="component">
          Gender: {{ gender }}
        </span>
        <span class="component">
          Faculty: {{ faculty }}
        </span>
        <span class="component">
          Interests: {{ interests.join(", ") }}
        </span>
        <span>
          <el-button @click="chat">
            Go Chat!
          </el-button>
        </span>
      </div>
    </el-card>
  </div>
</template>

<script>
import {ref} from "vue";
import router from "@/router";
import {useRoute, useRouter} from "vue-router";
import {Faculties, Genders, Interests} from "@/utils/UserUtil";

export default {
  name: "UserCard",
  props: {
    header: {
      default: 'https://cube.elemecdn.com/3/7c/3ea6beec64369c2642b92c6726f1epng.png'
    },
    lastPage: {
      required: true
    },
    name: {
      required: true
    },
    id: {
      required: true
    },
    gender: {
      required: true
    },
    faculty: {
      required: true
    },
    interests: {
      required: true
    },
  },
  setup({header: h, lastPage: lp, name: n, id: usr, gender: g, faculty: f, interests: i}) {
    const header = ref(h);
    const name = ref(n);
    const id = ref(usr);
    const gender = ref(Genders.value[g].name);
    const faculty = ref(Faculties.value[f].name);
    const interests = ref((() => {
      const l = [];
      for (let j = 0; j < i.length; j++) {
        if (i[j] !== 0) {
          l.push(Interests.value[j].name);
        }
      }
      return l;
    })());

    const route = useRoute();
    const router = useRouter();

    const chat = () => {
      router.push({
        name: "message",
        params: {
          opponent: id.value,
          lastPage: lp
        }
      })
    };
    return {
      header, name, id, gender, faculty, interests,
      chat
    }
  }
}
</script>

<style scoped>

.component {
  margin-left: 2%;
  margin-right: 2%;
  vertical-align: middle;
}

</style>