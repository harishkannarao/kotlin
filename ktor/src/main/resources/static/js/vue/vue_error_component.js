Vue.component('error-container', {
  props: ['error'],
  template: `
    <div>
        <div>
            <input type="image" src="/webjars/feather-icons/dist/icons/x-circle.svg" alt="Dismiss"
                                                 @click="$emit('clear-error', error)" class="qa-clear-error-btn"/>

             <span class="qa-error-message">{{ error.message }}</span>
         </div>
    </div>
  `
})