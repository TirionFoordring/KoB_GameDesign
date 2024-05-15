<template>
<nav class="navbar navbar-expand-lg navbar-dark bg-dark">
  <div class="container">

    <router-link class="navbar-brand" :to="{name: 'home'}">King of Bots</router-link>
    <button class="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#navbarNavAltMarkup" aria-controls="navbarNavAltMarkup" aria-expanded="false" aria-label="Toggle navigation">
      <span class="navbar-toggler-icon"></span>
    </button>
    <div class="collapse navbar-collapse" id="navbarNavAltMarkup">
      <div class="navbar-nav">
        <router-link :class="route_name === 'home' ? 'nav-link active' : 'nav-link'" :to="{name: 'home'}">Home</router-link>
        <router-link :class="route_name === 'record' ? 'nav-link active' : 'nav-link'" :to="{name: 'record'}">Games Record</router-link>
        <router-link :class="route_name === 'ranklist' ? 'nav-link active' : 'nav-link'" :to="{name: 'ranklist'}">Rank list</router-link>
      </div>
    </div>

    <!-- The UserCenter with dropdown menu on the right side of the Navbar-->
    <div class="navbar-nav">
      <li class="nav-item dropdown">
          <a class="nav-link dropdown-toggle" href="#" role="button" data-bs-toggle="dropdown" aria-expanded="false">
            User Center
          </a>
          <ul class="dropdown-menu">
            <li><router-link class="dropdown-item" :to="{name: 'user_bots'}">My Bots</router-link></li>
            <!-- <li><a class="dropdown-item" href="#">Another action</a></li> -->
            <li><hr class="drowdown-divider"></li>
            <li><a class="dropdown-item" href="#">log out</a></li>
          </ul>
        </li>
    </div>

  </div>
</nav>
</template>

<script>
//Judging the current page and highlight it in Navbar.
import { useRoute } from "vue-router"
import { computed } from "vue"

export default {
  setup() {
    const route = useRoute();
    let route_name = computed(() => route.name);
    return {
      route_name
    };
  }
}
</script>

<style scoped></style>