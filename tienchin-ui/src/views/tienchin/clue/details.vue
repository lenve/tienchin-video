<template>
  <div>
    <el-row>
      <el-col :span="18">
        <el-row>
          <el-card class="box-card">
            <template #header>
              <div class="card-header">
                <span>{{ clue.name }}/{{ clue.clueId }}</span>
              </div>
            </template>
            <div>
              <el-row>
                <el-col :span="8">
                  <div style="font-style: italic;font-weight: bold">线索创建时间</div>
                  <div style="color: #8392a6">{{ parseTime(clue.createTime) }}</div>
                </el-col>
                <el-col :span="8">
                  <div style="font-style: italic;font-weight: bold">客户手机号码</div>
                  <div style="color: #8392a6">{{ clue.phone }}</div>
                </el-col>
                <el-col :span="8">
                  <div style="font-style: italic;font-weight: bold">渠道来源</div>
                  <div style="color: #8392a6">{{ clue.channelName }}</div>
                </el-col>
              </el-row>
              <el-row>
                <el-col :span="8">
                  <div style="font-style: italic;font-weight: bold">线索分配者</div>
                  <div style="color: #8392a6">{{ clue.allocator }}</div>
                </el-col>
                <el-col :span="8">
                  <div style="font-style: italic;font-weight: bold">线索归属时间</div>
                  <div style="color: #8392a6">{{ parseTime(clue.belongTime) }}</div>
                </el-col>
                <el-col :span="8">
                  <div style="font-style: italic;font-weight: bold">线索归属人</div>
                  <div style="color: #8392a6">{{ clue.owner }}</div>
                </el-col>
              </el-row>
              <el-row>
                <el-col :span="16">
                  <div style="font-style: italic;font-weight: bold">所属促销活动信息</div>
                  <div style="color: #8392a6">{{ clue.activityName }}/{{ clue.activityInfo }}</div>
                </el-col>
                <el-col :span="8">
                  <el-button type="danger">无效线索</el-button>
                  <el-button type="primary">转为商机</el-button>
                </el-col>
              </el-row>
            </div>
          </el-card>
        </el-row>
        <el-row>
          <el-card class="box-card">
            <template #header>
              <div class="card-header">
                <span>线索跟进</span>
              </div>
            </template>
            <div>
              <el-row :gutter="36">
                <el-col :span="8">
                  <div style="font-style: italic;font-weight: bold">客户姓名</div>
                  <div>
                    <el-input :disabled="type=='view'" v-model="clue.name"></el-input>
                  </div>
                </el-col>
                <el-col :span="8">
                  <div style="font-style: italic;font-weight: bold">性别</div>
                  <div>
                    <el-select v-model="clue.gender" placeholder="请选择">
                      <el-option
                          v-for="dict in sys_user_sex"
                          :key="dict.value"
                          :label="dict.label"
                          :value="parseInt(dict.value)"
                      ></el-option>
                    </el-select>
                  </div>
                </el-col>
                <el-col :span="8">
                  <div style="font-style: italic;font-weight: bold">年龄</div>
                  <div>
                    <el-input-number :disabled="type=='view'" v-model="clue.age" :min="6" :max="99"/>
                  </div>
                </el-col>
              </el-row>
              <el-row :gutter="36">
                <el-col :span="8">
                  <div style="font-style: italic;font-weight: bold">客户微信号码</div>
                  <div>
                    <el-input :disabled="type=='view'" v-model="clue.weixin"></el-input>
                  </div>
                </el-col>
                <el-col :span="8">
                  <div style="font-style: italic;font-weight: bold">客户QQ号码</div>
                  <div>
                    <el-input :disabled="type=='view'" v-model="clue.qq"></el-input>
                  </div>
                </el-col>
                <el-col :span="8">
                  <div style="font-style: italic;font-weight: bold">客户手机号码</div>
                  <div>
                    <el-input :disabled="type=='view'" v-model="clue.phone"></el-input>
                  </div>
                </el-col>
              </el-row>
              <el-row>
                <el-col :span="24">
                  <div style="font-style: italic;font-weight: bold">客户意向等级</div>
                  <div>
                    <el-radio-group v-model="clue.level" :disabled="type=='view'">
                      <el-radio :label="cl.value" :key="index" v-for="(cl,index) in clue_level">{{
                          cl.label
                        }}
                      </el-radio>
                    </el-radio-group>
                  </div>
                </el-col>
              </el-row>
              <el-row>
                <el-col :span="24">
                  <div style="font-style: italic;font-weight: bold">客户意向类型</div>
                  <div>
                    <el-radio-group v-model="clue.subject" :disabled="type=='view'">
                      <el-radio :label="ct.value" :key="index" v-for="(ct,index) in course_type">{{
                          ct.label
                        }}
                      </el-radio>
                    </el-radio-group>
                  </div>
                </el-col>
              </el-row>
              <el-row>
                <el-col :span="24">
                  <div style="font-style: italic;font-weight: bold">跟进记录</div>
                  <div>
                    <el-input
                        v-model="clue.record"
                        :rows="5"
                        type="textarea"
                        :disabled="type=='view'"
                        placeholder="请输入内容"
                    />
                  </div>
                </el-col>
              </el-row>
              <el-row>
                <el-col :span="24">
                  <div style="font-style: italic;font-weight: bold">下次跟进时间</div>
                  <div>
                    <el-date-picker
                        v-model="clue.nextTime"
                        type="datetime"
                        :disabled="type=='view'"
                        clearable
                        value-format="YYYY-MM-DD hh:mm:ss"
                        format="YYYY-MM-DD hh:mm:ss"
                        placeholder="请选择下次跟进时间"
                    />
                  </div>
                </el-col>
              </el-row>
              <el-row>
                <el-col>
                  <div style="display: flex;justify-content: flex-end">
                    <el-button :disabled="type=='view'" type="primary">提交</el-button>
                    <el-button>返回</el-button>
                  </div>
                </el-col>
              </el-row>
            </div>
          </el-card>
        </el-row>
      </el-col>
      <el-col :span="6"></el-col>
    </el-row>
  </div>
</template>

<script setup>
import {getCurrentInstance, onMounted} from "vue";
import {getClueById} from "../../../api/tienchin/clue";
import {parseTime} from "../../../utils/ruoyi";

const {proxy} = getCurrentInstance();
const {sys_user_sex, clue_level,course_type} = proxy.useDict("sys_user_sex", "clue_level","course_type");

const type = ref("");
const clue = ref({});


const data = reactive({
  name: 'zhangsan'
});

const {name} = toRefs(data);

/**
 * 跳转到这个页面的请求格式是 /clue/details/index/10/view
 */
onMounted(() => {
  const clueId = proxy.$route.params && proxy.$route.params.clueId;
  type.value = proxy.$route.params && proxy.$route.params.type;
  handleClue(clueId);
})

function handleClue(clueId) {
  getClueById(clueId).then(response => {
    clue.value = response.data;
  })
}

</script>

<style scoped>
.box-card {
  margin-top: 10px;
  width: 100%;
}

.el-row {
  margin-bottom: 30px;
}

.el-row:last-child {
  margin-bottom: 10px;
}
</style>
