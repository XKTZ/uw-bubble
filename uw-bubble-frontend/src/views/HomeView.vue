<template>
  <div></div>
</template>

<script>
// @ is an alias to /src

import router from "@/router";

export default {
  name: 'HomeView',
  components: {
  },
  setup() {
    router.push("/recommend")
  }
}
</script>
