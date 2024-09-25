import * as React from "react";
import { useForm } from "antd/es/form/Form";
import {IProductCreate} from "./types"
import { Button, Form, Input, Modal, Row, Upload, UploadFile } from "antd";
import TextArea from "antd/es/input/TextArea";
import { PlusOutlined } from '@ant-design/icons';
import axios from "axios";
import { useState } from "react";
import { RcFile, UploadChangeParam } from "antd/es/upload";

const ProductCreatePage: React.FC = () => {
  const [form] = useForm<IProductCreate>();

  const [previewOpen, setPreviewOpen] = useState<boolean>(false);
  const [previewImage, setPreviewImage] = useState('');
  const [previewTitle, setPreviewTitle] = useState('');

  const onSubmitForm = async (values: IProductCreate) => {
    const url = "http://localhost:8888/api/product";
    try {
      const id = await axios.post<number>(url, values, {
        headers: {
          "Content-Type": "multipart/form-data"
        }
      });
      console.log("Product created with ID: ", id);
    } catch (e) {
      console.log("Error", e);
    }
  };

  return (
    <>
      <div className="py-12">
        <div className="mx-auto max-w-7xl px-6 lg:px-24">
          <p className="text-center text-3xl font-bold tracking-tight text-gray-900 sm:text-4xl">
            Створення продукту
          </p>
          <Form
            form={form}
            onFinish={onSubmitForm}
            layout={"vertical"}
          >
            <Form.Item
              label={"Назва"}
              name="name"
              rules={[{ required: true, message: "Вкажіть назву продукту" }]}
            >
              <Input autoComplete={"name"} />
            </Form.Item>

            <Form.Item
              label={"Опис"}
              name="description"
              rules={[{ required: true, message: "Вкажіть опис продукту" }]}
            >
              <TextArea autoComplete={"description"} rows={3} />
            </Form.Item>

            <Form.Item
              label={"Ціна"}
              name="price"
              rules={[{ required: true, message: "Вкажіть ціну продукту" }]}
            >
              <Input type="number" autoComplete={"price"} />
            </Form.Item>

            <Form.Item
              label={"Знижка"}
              name="discount"
              rules={[{ required: true, message: "Вкажіть знижку" }]}
            >
              <Input type="number" autoComplete={"discount"} />
            </Form.Item>

            <Form.Item
              label={"Категорія"}
              name="categoryId"
              rules={[{ required: true, message: "Оберіть категорію" }]}
            >
              <Input type="number" autoComplete={"categoryId"} />
            </Form.Item>

            <Form.Item
              label="Оберіть фото"
              name="images"
              valuePropName="images"
              getValueFromEvent={(e: UploadChangeParam) => e.fileList.map(file => file.originFileObj)}
              rules={[{ required: true, message: "Оберіть принаймні одну фотографію" }]}
            >
              <Upload
                listType="picture-card"
                beforeUpload={() => false}
                multiple
                accept="image/png, image/jpeg, image/webp"
                onPreview={(file: UploadFile) => {
                  if (!file.url && !file.preview) {
                    file.preview = URL.createObjectURL(file.originFileObj as RcFile);
                  }
                  setPreviewImage(file.url || file.preview as string);
                  setPreviewOpen(true);
                  setPreviewTitle(file.name || file.url!.substring(file.url!.lastIndexOf('/') + 1));
                }}
              >
                <div>
                  <PlusOutlined />
                  <div>Upload</div>
                </div>
              </Upload>
            </Form.Item>

            <Row style={{ display: 'flex', justifyContent: 'center' }}>
              <Button type="primary" htmlType="submit">Додати продукт</Button>
            </Row>
          </Form>
        </div>
      </div>

      <Modal open={previewOpen} title={previewTitle} footer={null} onCancel={() => setPreviewOpen(false)}>
        <img alt="preview" style={{ width: '100%' }} src={previewImage} />
      </Modal>
    </>
  );
};

export default ProductCreatePage;
