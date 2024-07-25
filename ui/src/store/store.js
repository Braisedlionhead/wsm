import { reactive, watchEffect } from 'vue'

const initialCollapseState = localStorage.getItem('collapseState')
const initialCollapse = initialCollapseState ? JSON.parse(initialCollapseState) : false

const storage = reactive({
    collapse: initialCollapse,
})

watchEffect(() => {
    localStorage.setItem('collapseState', JSON.stringify(storage.collapse))
})


export default storage


