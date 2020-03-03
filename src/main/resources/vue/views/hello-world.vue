<template id="hello-world">
    <h1 class="hello-world">Hello, World 2!</h1>
</template>

<!-- Definition einer Komponente auf Basis einer Vorlage. -->
<script>
    Vue.component("hello-world", {template: "#hello-world"});
</script>

<!-- CSS-Regeln für die Vorlagengestaltung. -->
<style>
    .hello-world {
        color: goldenrod;
    }
</style>
