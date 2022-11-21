<template>
  <div>
    <el-container>
      <el-header>

      </el-header>
      <el-main>
        <div style="width: 100%;">
        <OpponentMessageComponent
            v-for="m in messages"
            :message="m.text"
            />
        </div>
      </el-main>
      <el-footer>

      </el-footer>
    </el-container>

  </div>
</template>

<script>

import {onMounted, ref} from "vue";
import {useRoute, useRouter} from 'vue-router';
import router from "@/router";
import OpponentMessageComponent from "@/components/OpponentMessageComponent";

export default {
  name: "MessageView",
  components: {OpponentMessageComponent},
  setup() {
    const router = useRouter();
    const route = useRoute();

    const messages = ref([
      {
        text: "Lorem ipsum dolor sit amet, consectetur adipisicing elit. Aspernatur debitis dicta dolorem dolores eos est eum eveniet explicabo in laborum maiores maxime natus placeat qui quibusdam repudiandae temporibus ut, vero?"
      }
    ]);

    onMounted(() => {

    });

    return {
      messages
    }
  }
}
</script>

<style scoped>

</style>