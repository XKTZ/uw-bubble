<template>
  <div>
    <el-container>
      <el-header
          :style="{height: headerHeight, fontSize: '24px', fontWeight: '650', paddingTop: '20px', paddingBottom: '20px'}">
        <el-row>
          <el-col :span="4">
            <el-button @click="back" style="width: 80%; height: 100%;">
              Back
            </el-button>
          </el-col>
          <el-col :span="16">
            {{ opponentName }}
          </el-col>
          <el-col :span="4"></el-col>
        </el-row>
      </el-header>
      <el-main>
        <div style="width: 100%;">

          <el-scrollbar ref="scrollBar" :height="messagesHeight">
            <message-component v-for="m in messages"
                               :message="m.text"
                               :type="m.type"
            />
          </el-scrollbar>
        </div>
        <div style="padding-top: 1%; padding-bottom: 1%; height: 40px;">
          <el-input :rows="2" type="textarea"
                    v-on:keydown.ctrl.enter="onSend"
                    v-model="messageInput" placeholder="Your Message"/>
        </div>
      </el-main>
    </el-container>
  </div>
</template>

<script>

import {onMounted, onUnmounted, ref} from "vue";
import {useRoute, useRouter} from 'vue-router';
import MessageComponent from "@/components/MessageComponent";
import {assertLogin, getId, MessageSender} from "@/utils/UserUtil";
import {getReactiveVariable, registerReactiveVariable} from "@/utils/windows";
import axios from "axios";
import {ElScrollbar} from 'element-plus';

registerReactiveVariable('messageHeaderHeight', (w, h) => {
  return h * .02;
});

registerReactiveVariable('messagesHeight', (w, h) => {
  return h * .7;
});

export default {
  name: "MessageView",
  components: {MessageComponent},
  props: {
    opponent: {
      required: true
    },
    lastPage: {
      required: true
    }
  },
  setup({opponent: o, lastPage}) {
    lastPage = parseInt(lastPage);

    const router = useRouter();
    const route = useRoute();

    const messages = ref([]);
    let last = messages.value.length;

    const messageInput = ref();

    const id = ref(getId());
    const opponentId = ref(parseInt(o));

    const headerHeight = getReactiveVariable('messageHeaderHeight');
    const messagesHeight = getReactiveVariable("messagesHeight");
    const opponentName = ref("Match");

    const scrollBar = ref();

    const refresher = ref(undefined);

    const getOpponentId = () => {
      return opponentId.value;
    }

    const scrolling = () => {
      scrollBar.value.setScrollTop(10000);
    }

    const refreshMessages = async () => {
      const id = getOpponentId();
      const jwt = localStorage.getItem('jwt');
      const msgs = (await axios.get('/message/find/', {
        headers: {
          authorization: `Bearer ${jwt}`
        },
        params: {
          pairId: id
        }
      })).data;
      await (messages.value = msgs.map(x => {
        if (x.sender === id) {
          return {
            text: x.content,
            type: MessageSender.Opponent
          }
        } else {
          return {
            text: x.content,
            type: MessageSender.User
          }
        }
      }));
      if (messages.value.length !== last) {
        scrolling();
        last = messages.value.length;
      }
    };

    const sendMessage = async () => {
      try {
        const id = getOpponentId();
        const jwt = localStorage.getItem('jwt');
        const result = await axios({
          method: "post",
          url: '/message/publish/',
          headers: {
            authorization: `Bearer ${jwt}`
          },
          data: {
            content: messageInput.value,
            recipient: id,
            type: "text"
          }
        });
        messageInput.value = '';
      } catch (e) {
        console.log(e)
      }
    };

    const back = () => {
      if (lastPage === 0) {
        router.push('/recommend')
      } else {
        router.push('/communications')
      }

    }

    onMounted(async () => {
      await assertLogin();
      await refreshMessages();
      await (refresher.value = setInterval(refreshMessages, 2500));
    });

    const onSend = async (e) => {
      e.preventDefault();
      await sendMessage();
      await refreshMessages();
    };

    onMounted(() => {
      headerHeight.value = window.innerHeight * .02;
    });

    onMounted(() => {
      messagesHeight.value = window.innerHeight * .7;
    });

    onUnmounted(() => {
      clearInterval(refresher.value)
    });

    return {
      opponentName,

      messages,
      headerHeight,
      messagesHeight,

      messageInput,
      onSend,

      MessageSender,

      scrollBar,

      back
    }
  }
}
</script>

<style scoped>

</style>