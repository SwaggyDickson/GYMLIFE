// CKEditor Use
ClassicEditor
.create(document.querySelector('#activityInfo'), {
    extraPlugins: [CustomUploadAdapterPlugin],

    
    customUploadAdapter: {
        uploadUrl: 'http://localhost:8080/gymlife/activity/api/upload'
    }
})
.then(editor => {
    console.log(editor);
})
.catch(error => {
    console.error(error);
});

function CustomUploadAdapterPlugin(editor) {
editor.plugins.get('FileRepository').createUploadAdapter = loader => {
    return new CustomUploadAdapter(loader, editor.config.get('customUploadAdapter.uploadUrl'));
};
}


class CustomUploadAdapter {
    constructor(loader, uploadUrl) {
        this.loader = loader;
        this.uploadUrl = uploadUrl;
    }

    upload() {
        return this.loader.file
            .then(file => new Promise((resolve, reject) => {
                this._uploadFile(file).then(response => {
                    if (response.url) {
                        resolve({ default: response.url });
                    } else {
                        reject(`Upload failed: ${response.message}`);
                    }
                });
            }));
    }

    _uploadFile(file) {
        const data = new FormData();
        data.append('file', file);

        return fetch(this.uploadUrl, {
            method: 'POST',
            body: data
        })
            .then(response => response.json())
            .catch(error => {
                console.error('Upload error:', error);
                throw error;
            });
    }
}