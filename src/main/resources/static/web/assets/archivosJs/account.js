let {createApp} = Vue

createApp({
    data(){
        return {
            account:[],
            getId:"",
            transactions:[],
            
            
        }
    },

    created(){
        this.loadData()

    },


    methods:{
        loadData(){
            this.getId =new URLSearchParams(location.search).get("id")
            axios.get(`http://localhost:8080/api/accounts/${this.getId}`)
            .then((res) => {
                this.account = res.data
                console.log(this.account)
                this.transactions = this.account.transactions
                this.transactions.sort((itemA, itemB)=> itemA.id - itemB.id)
                console.log(this.transactions)
                

            })
            .catch(err => console.log (err))
        },
    }

}).mount ("#app")