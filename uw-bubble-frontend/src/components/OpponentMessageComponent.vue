<template>
  <div>
    <el-avatar class="header" :size="40" :src="headerUrl"/>

    <span>
      {{message}}
    </span>
  </div>
</template>

<script>
import {ref} from "vue";

export default {
  name: "OpponentMessageComponent",
  props: {
    header: {
      default: 'https://cube.elemecdn.com/3/7c/3ea6beec64369c2642b92c6726f1epng.png'
    },
    message: {
      required: true
    }
  },
  setup(props) {
    const headerUrl = ref(props.header);
    const message = ref(props.message);

    return {
      headerUrl,
      message
    }
  }
}
</script>

<style scoped>

.header {
  margin-top: 1%;
  margin-right: 1%;
}

</style>