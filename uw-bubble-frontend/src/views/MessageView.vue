<template>
  <div>
    <el-container>
      <el-header
          :style="{height: headerHeight, fontSize: '24px', fontWeight: '650', paddingTop: '20px', paddingBottom: '20px'}">
        {{ opponentName }}
      </el-header>
      <el-main>
        <div style="width: 100%;">

          <el-scrollbar ref="scrollBar" :height="messagesHeight">
            <opponent-message-component v-for="m in messages"
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

import {onMounted, ref} from "vue";
import {useRoute, useRouter} from 'vue-router';
import OpponentMessageComponent from "@/components/MessageComponent";
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
  components: {OpponentMessageComponent},
  setup() {
    const router = useRouter();
    const route = useRoute();
    const messages = ref([]);
    let last = messages.value.length;

    const messageInput = ref();

    const id = ref(getId());
    const opponentId = ref(undefined);

    const headerHeight = getReactiveVariable('messageHeaderHeight');
    const messagesHeight = getReactiveVariable("messagesHeight");
    const opponentName = ref("Opponent");

    const scrollBar = ref();

    const getOpponentId = () => {
      if (opponentId.value === undefined) {
        if (route.params.id === undefined) {
          return opponentId.value = parseInt(prompt("Opponent's id"));
        } else {
          return opponentId.value = route.params.id;
        }
      } else {
        return opponentId.value;
      }
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

    onMounted(async () => {
      await assertLogin();
      await refreshMessages();
      setInterval(refreshMessages, 2500);
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

    return {
      opponentName,

      messages,
      headerHeight,
      messagesHeight,

      messageInput,
      onSend,

      MessageSender,

      scrollBar
    }
  }
}
</script>

<style scoped>

</style>