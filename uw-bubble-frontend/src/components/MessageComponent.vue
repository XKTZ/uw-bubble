<template>
  <div :style="{marginTop: '2%', marginBottom: '2%', width: '100%'}">
    <el-row>
      <el-col :span="avatarSize">
        <el-avatar v-if="type === MessageSender.Opponent" class="header" :size="40" :src="headerUrl"/>
      </el-col>
      <el-col :span="messageSize" :style="
      {
        textAlign: 'justify',
        backgroundColor: color,
        borderRadius: '8px',
        padding: '10px',
        fontSize: messageFontSize,
        color: fontColor
      }
">
        {{ message }}
      </el-col>
      <el-col :span="avatarSize">
        <el-avatar v-if="type === MessageSender.User" class="header" :size="40" :src="headerUrl"/>
      </el-col>
    </el-row>
  </div>
</template>

<script>
import {ref} from "vue";
import {getReactiveVariable, registerReactiveVariable} from "@/utils/windows";
import {MessageSender} from "@/utils/UserUtil";

registerReactiveVariable("avatarSize", (w, h) => {
  if (w >= 500) {
    return 3;
  } else {
    return 6;
  }
});

registerReactiveVariable("messageSize", (w, h) => {
  if (w >= 500) {
    return 18;
  } else {
    return 12;
  }
});

registerReactiveVariable("messageFontSize", (w, h) => {
  if (w < 500) {
    return "14px";
  } else {
    return "16px";
  }
})

export default {
  name: "MessageComponent",
  props: {
    header: {
      default: 'https://cube.elemecdn.com/3/7c/3ea6beec64369c2642b92c6726f1epng.png'
    },
    message: {
      required: true,
    },
    type: {
      required: true
    }
  },
  setup({header, message: msg, type}) {
    const headerUrl = ref(header);
    const message = ref(msg);
    const color = ref(type === MessageSender.User ? '#42a5f5': '#f7f7f7');
    const fontColor = ref(type === MessageSender.User ? 'white' : 'black');
    const floating = ref(type === MessageSender.User ? 'right' : 'left');

    const avatarSize = getReactiveVariable('avatarSize');
    const messageSize = getReactiveVariable('messageSize');
    const messageFontSize = getReactiveVariable("messageFontSize");

    return {
      headerUrl,
      message,
      type,
      color,
      fontColor,
      floating,

      avatarSize,
      messageSize,
      messageFontSize,

      MessageSender
    }
  }
}
</script>

<style scoped>

.header {
}

</style>