<script>
    import {fetchAPI} from "@/utils/fetchAPI";

    export default {
        data() {
            return {
                message: null,
                FormData: {
                    email: null
                }
            }
        },
        methods: {
            async resetPassword() {
                let request = await fetchAPI('/token', 'POST', this.FormData);
                let data = await request.json();
                if(request.ok) {
                    this.message = "Check your email address";                
                    this.FormData.email = null;
                }
            }
        }
    }
</script>



<template>
    <div class="form-holder">
        <form @submit.prevent="resetPassword">
            <div>
                <label for="email">Email</label>
                <input type="email" id="email" v-model="FormData.email">
            </div>
            <button type="submit">Reset your password</button>
            {{ message }}
        </form>
    </div>
</template>


<style scoped>
    
</style>