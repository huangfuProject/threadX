<template>
    <div class="common-layout">
      <el-container>
        <el-aside>
          <AsideComponents/>
        </el-aside>

        <el-main>
          <div class="topDiv">
            <el-row>
            <el-col :span="24">
              <TopComponents/>
            </el-col>
          </el-row>


          </div>

          <div class="mainDiv">
            <el-row>
            <el-col :span="24">
              <router-view/>
            </el-col>
          </el-row>
          </div>
        </el-main>
      </el-container>
  </div>
</template>

<script lang="ts">
import { defineComponent } from 'vue'
import AsideComponents from '@/components/AsideComponents.vue';
import TopComponents from '@/components/TopComponents.vue';

export default defineComponent({
    setup () {
        

        return {}
    },
    components: {
        AsideComponents,
        TopComponents
    }
})
</script>

<style scoped>
  .el-aside{
      width: var(--el-aside-width,90px);
  }

  .topDiv {
    background-color: #f0f0f0;
  }

  .mainDiv {
    background-color: #f0f0f0;
    margin-top: 10px;
  }

  .el-main {
    --el-main-padding: 10px;
  }
</style>